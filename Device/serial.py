class Serial:
	def __init__(self, serial_type, port, timeout=0):
		self.serial_type = serial_type
		self.port = port
		self.timeout = timeout
		
		self.set_value('temp', 22)
	
	
	requests = {'light' : 0, 'button1' : 0, 'button2' : 0, 'knob' : 0, 'temp' : 0, 'motion' : 0}
	headers = {'light' : 0, 'button1' : 194, 'button2' : 196, 'knob' : 64, 'temp' : 128, 'motion' : 192}
	values = {'light' : 0, 'button1' : 0, 'button2' : 0, 'knob' : 0, 'temp' : 0, 'motion' : 0, 'dashboard' : 0, 'led1' : 0}
	
	
	def write(self, char_value):
		value = ord(char_value)
		
		if value < 32:
			self.values['dashboard'] = value
		elif value < 34:
			self.values['led1'] = value - 32
		elif value < 64:
			pass
		elif value < 128:
			self.values['light'] = value - 64
		elif value < 192:
			self.handle_request(value - 128)
		else:
			self.handle_request(value - 192)

	
	def handle_request(self, request):
		self.requests['light'] = 1 if request > 31 else 0
		self.requests['button1'] = 1 if request - self.requests['light'] > 15 else 0
		self.requests['button2'] = 1 if request - self.requests['light'] - self.requests['button1'] > 7 else 0
		self.requests['knob'] = 1 if request - self.requests['light'] - self.requests['button1'] - self.requests['button2'] > 3 else 0
		self.requests['temp'] = 1 if request - self.requests['light'] - self.requests['button1'] - self.requests['button2'] - self.requests['knob'] > 1 else 0
		self.requests['motion'] = request - self.requests['light'] - self.requests['button1'] - self.requests['button2'] - self.requests['knob'] - self.requests['temp']

		
	def read(self, bytes):
		# Todo: Should we handle number of bytes to read here?
		for key, value in self.requests.iteritems():
			if value == 1:
				return chr(self.headers[key] + self.values[key])
	
	
	def set_value(self, key, value):
		self.values[key] = value
