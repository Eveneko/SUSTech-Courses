from http.server import HTTPServer, BaseHTTPRequestHandler, SimpleHTTPRequestHandler

Handler = SimpleHTTPRequestHandler
httpd = HTTPServer(('0.0.0.0', 8081), Handler)
httpd.serve_forever()