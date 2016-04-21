import serial


class Rgb3:
    def __init__(self, r, g, b):
        if not (r in range(0, 4) and g in range(0, 4) and b in range(0, 4)):
            raise ValueError("wrong rgb (each 0-3)")
        self.r = r
        self.g = g
        self.b = b


class QueryableComponent:
    def __init__(self, request_value, response_header):
        self.request_value = request_value
        self.response_header = response_header


QueryableComponent.LIGHT = QueryableComponent(32, 0)
QueryableComponent.BUTTON1 = QueryableComponent(16, 128 + 64 + 2)
QueryableComponent.BUTTON2 = QueryableComponent(8, 128 + 64 + 4)
QueryableComponent.KNOB = QueryableComponent(4, 64)
QueryableComponent.TEMP = QueryableComponent(2, 128)
QueryableComponent.MOTION = QueryableComponent(1, 128 + 64)


class Request:
    def __init__(self, char_value):
        self.char_value = char_value

    @classmethod
    def to_set_dashboard_angle(cls, angle):
        if angle < 0 or angle > 31:
            raise ValueError("wrong angle (0-31)")
        return Request(chr(128 + angle))

    @classmethod
    def to_set_led1(cls, value):
        if value != 0 and value != 1:
            raise ValueError("wrong value (0-1)")
        return Request(chr(32 + value))

    @classmethod
    def to_set_rgb_of_led2(cls, rgb):
        return Request(chr(64 + 16 * rgb.r + 4 * rgb.g + rgb.b))

    @classmethod
    def for_automatic_updates_of(cls, *args):
        val = 0
        for component in args:
            val += component.request_value
        return Request(chr(128 + val))

    @classmethod
    def query_for_parameters_of(cls, *args):
        val = 0
        for component in args:
            val += component.request_value
        return Request(chr(128 + 64 + val))

class Response:
    def __init__(self, value):
        self.value = ord(value)

    def value_of(self, component):
        return self.value - component.response_header


class Copernicus:
    def __init__(self):
        self.serial = serial.Serial('/dev/ttyS0', 38400, timeout=1)

    def send_request(self, req):
        self.serial.write(req.char_value)

    def get_response(self):
        cc = ""
        while len(cc) <= 0:
            cc = self.serial.read(1)
        return Response(cc)
