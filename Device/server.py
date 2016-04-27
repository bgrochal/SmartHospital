import os
import sys
import time
import thread
import datetime

from twisted.internet import defer
from twisted.internet.protocol import DatagramProtocol
from twisted.internet import reactor
from twisted.python import log

import txthings.resource as resource
import txthings.coap as coap

import ca_short as ca


class CoreResource(resource.CoAPResource):
    """
    Example Resource that provides list of links hosted by a server.
    Normally it should be hosted at /.well-known/core
    Resource should be initialized with "root" resource, which can be used
    to generate the list of links.
    For the response, an option "Content-Format" is set to value 40,
    meaning "application/link-format". Without it most clients won't
    be able to automatically interpret the link format.
    Notice that self.visible is not set - that means that resource won't
    be listed in the link format it hosts.
    """

    def __init__(self, root):
        resource.CoAPResource.__init__(self)
        self.root = root

    def render_GET(self, request):
        data = []
        self.root.generateResourceList(data, "")
        payload = ",".join(data)
        print payload
        response = coap.Message(code=coap.CONTENT, payload=payload)
        response.opt.content_format = coap.media_types_rev['application/link-format']
        return defer.succeed(response)


class BedResource(resource.CoAPResource):
	def __init__(self):
		resource.CoAPResource.__init__(self)
		self.visible = True
		
	def render_GET(self, request):
		global bed_position
		response_message = coap.Message(code=coap.CONTENT, payload='Bed position is: %d.' % bed_position)
		return defer.succeed(response_message)
	
	def render_PUT(self, request):
		global bed_position
		bed_position = (int)(request.payload)
		ca.set_dash(bed_position)
		response_message = coap.Message(code=coap.CONTENT, payload="Bed position set to: %d." % bed_position)
		return defer.succeed(response_message)


class TemperatureResource(resource.CoAPResource):
	def __init__(self):
		resource.CoAPResource.__init__(self)
		self.visible = True
		self.observable = True
		self.notify()
		
	def render_GET(self, request):
		ca.query(ca.TEMP)
		response_value = ca.get_res().value_of(ca.TEMP)
		response_value = (response_value - 10) * 2
		response_message = coap.Message(code=coap.CONTENT, payload="Temperature of patient is: %d." % response_value)
		
		temperature_file = open("temperatures.txt", "a")
		temperature_file.write(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S") + " " + (str)(response_value) + "\n")
		temperature_file.close()
		
		return defer.succeed(response_message)

	def notify(self):
		self.updatedState()
		reactor.callLater(5, self.notify)


class TemperatureFileResource(resource.CoAPResource):
	def __init__(self):
		resource.CoAPResource.__init__(self)
		self.visible = True
	
	def render_GET(self, request):
		temperature_file = open("temperatures.txt", "r")
		lines_content = temperature_file.readlines()
		string_content = "".join(lines_content)
		response_message = coap.Message(code=coap.CONTENT, payload=string_content)
		return defer.succeed(response_message)
	
	def render_DELETE(self, request):
		os.remove("temperatures.txt")
		response_message = coap.Message(code=coap.CONTENT, payload="History of temperatures deleted successfully.")
		return defer.succeed(response_message)


class TemperatureProgramResource(resource.CoAPResource):
	def __init__(self):
		resource.CoAPResource.__init__(self)
		self.visible = True
		self.observable = True
	
	def render_GET(self, request):
		program_file = open("program_output.txt", "r")
		lines_content = program_file.readlines()
		string_content = "".join(lines_content)
		response_message = coap.Message(code=coap.CONTENT, payload=string_content)
		return defer.succeed(response_message)
	
	def thread_function(self, duration, arguments):
		program_file = open("program_output.txt", "w")
		for argument in arguments:
			ca.query(ca.TEMP)
			response_value = ca.get_res().value_of(ca.TEMP)
			response_value = (response_value - 10) * 2
			program_file.write(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S") + " Expected: " + argument + "; is: " + (str)(response_value) + ".\n")
			time.sleep(duration)
		program_file.close()
	
	def render_PUT(self, request):
		arguments = (request.payload).split(" ")
		duration = (int)(arguments[0])
		arguments.pop(0)
		response_message = coap.Message(code=coap.CONTENT, payload="Program registered and started successfully.")
		thread.start_new_thread(self.thread_function, (duration, arguments))
		return defer.succeed(response_message)


class AlertResource(resource.CoAPResource):
	def __init__(self):
		resource.CoAPResource.__init__(self)
		self.visible = True
		self.observable = True
		self.notify()
	
	def render_GET(self, request):
		pass
	
	def notify(self):
		self.updatedState()
		reactor.callLater(5, self.notify)


# Global logic
bed_position = 0

# Resource tree creation
log.startLogging(sys.stdout)
root = resource.CoAPResource()

well_known = resource.CoAPResource()
root.putChild('.well-known', well_known)
core = CoreResource(root)
well_known.putChild('core', core)

hospital = resource.CoAPResource()
root.putChild('Hospital', hospital)

alert = AlertResource()
hospital.putChild('Alert', alert)

bed = BedResource()
hospital.putChild('Bed', bed)

temperature = TemperatureResource()
hospital.putChild('Temperature', temperature)

temperature_file = TemperatureFileResource()
hospital.putChild('TemperaturesHistory', temperature_file)

temperature_program = TemperatureProgramResource()
hospital.putChild('TemperatureProgram', temperature_program)

# Run server.
endpoint = resource.Endpoint(root)
reactor.listenUDP(coap.COAP_PORT, coap.Coap(endpoint)) #, interface="::")
reactor.run()
