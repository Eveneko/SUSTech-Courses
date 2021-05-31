import mimetypes
import asyncio
import os
import re
from urllib.parse import unquote


class HTTPHeader:
    def __init__(self):
        self.headers = {'method': '', 'path': ''}
        self.range = ['0']
        self.cookie = None

    def parse_header(self, line):
        print(line, end='')
        filed = unquote(line, encoding='utf-8').split(' ')
        # range
        if filed[0] == 'Range:':
            self.range = re.findall(r'\d+', filed[1])
        # method
        if str.upper(filed[0]) == 'GET' or str.upper(filed[0]) == 'HEAD':
            self.headers['method'] = str.upper(filed[0])
            path = filed[1]
            if len(filed) > 3:
                for i in range(2, len(filed) - 1):
                    path += ' ' + filed[i]
            self.headers['path'] = path
        # cookie
        if filed[0] == 'Cookie:':
            self.cookie = filed[1]

    def get(self, key):
        return self.headers.get(key)


class HTTPResponse:

    def __init__(self, status_code, content_length, content_type, body, cookie_id):
        self.status_phrase = {200: 'OK', 206: 'Partial Content', 302: 'Found', 404: 'Not Found',
                              405: 'Method Not Allowed'}
        self.version = 'HTTP/1.1'
        self.status_code = status_code
        self.phrase = self.status_phrase[status_code]
        self.content_length = content_length
        self.content_type = content_type
        self.body = body
        self.connection = 'close'
        self.set_cookie = False
        self.cookie_id = cookie_id

    def http_response_get(self):
        return str.encode('{version} {status_code} {phrase}\r\n'
                          'Connection: {connection}\r\n'
                          'Accept-Ranges: bytes\r\n'
                          '{cookie}'
                          'Content-Length: {content_length}; charset=utf-8\r\n'
                          'Content-Type: {content_type}\r\n'
                          '\r\n'.format(version=self.version, status_code=str(self.status_code),
                                        phrase=self.phrase, content_length=str(self.content_length),
                                        content_type=self.content_type, connection=self.connection,
                                        cookie='Set-Cookie:' +
                                               self.cookie_id if self.set_cookie is False else '')) + self.body

    def http_response_head(self):
        return str.encode('{version} {status_code} {phrase}\r\n'
                          'Connection: close\r\n'
                          'Accept-Ranges: bytes\r\n'
                          'Content-Length: {content_length}; charset=utf-8\r\n'
                          'Content-Type: {content_type}\r\n'
                          '\r\n'.format(version=self.version, status_code=self.status_code,
                                        phrase=self.phrase, content_length=self.content_length,
                                        content_type=self.content_type))

    def location_302(self, location):
        return str.encode('{version} {status_code} {phrase}\r\n'
                          'Location: http://127.0.0.1:8080{location}'.format(version=self.version,
                                                                             status_code=self.status_code,
                                                                             phrase=self.phrase, location=location))


def html(content):
    return str.encode('<html><body>{content}</body></html>'.format(content=content))


def utf8len(line):
    return len(line)


user_lastDir = {}
cookie_id = 0


async def dispatch(reader, writer):
    header = HTTPHeader()
    while True:
        data = await reader.readline()
        message = data.decode()
        header.parse_header(message)
        if data == b'\r\n':
            break
    # NOT SUPPORT method
    http_response = HTTPResponse(200, 0, None, None, '0')
    # set cookie_id
    global cookie_id
    global user_lastDir
    # if request does not have cookie, set cookie
    if header.cookie is None:
        cookie_id += 1
        http_response.cookie_id = str(cookie_id) + ' '
        header.cookie = http_response.cookie_id
    else:
        http_response.set_cookie = True
    print('cookie: ' + str(header.cookie))

    if header.get('method') == '':
        http_response.status_code = 405
        http_response.content_length = utf8len(html('405 Method Not Allowed'))
        http_response.content_type = 'text/html'
        http_response.body = html('405 Method Not Allowed')
        writer.writelines(http_response.http_response_get())
    # GET method    
    elif header.get('method') == 'GET':
        path = os.getcwd() + header.get('path')
        if user_lastDir.get(header.cookie) is not None and header.get('path') == '/':
            http_response.status_code = 302
            writer.write(http_response.location_302(user_lastDir.get(header.cookie)))
        else:
            user_lastDir[header.cookie] = header.get('path')
            if os.path.exists(path):
                if os.path.isdir(path):
                    message = render_html(path, header.get('path'))
                    http_response.content_length = utf8len(message)
                    http_response.content_type = 'text/html'
                    http_response.body = message
                    writer.write((http_response.http_response_get()))

                else:
                    file = open(path, 'rb')
                    file_type = mimetypes.guess_type(header.get('path'))[0]
                    if file_type is None:
                        file_type = 'application/octet-stream'

                    if header.range[0] != '0':
                        file.seek(int(header.range[0]))
                        http_response.status_code = 206
                        http_response.content_length = str(os.path.getsize(path) - int(header.range[0]))
                        http_response.content_type = file_type
                        http_response.body = file.read()
                        http_response.connection = 'keep-alive'
                        writer.write(http_response.http_response_get())
                    else:
                        http_response.content_length = str(os.path.getsize(path))
                        http_response.content_type = file_type
                        http_response.body = file.read()
                        writer.write(http_response.http_response_get())

            else:
                http_response.status_code = 404
                http_response.content_length = utf8len(html('404 Not Found'))
                http_response.content_type = 'text/html'
                http_response.body = html('404 Not Found')
                writer.write(http_response.http_response_get())
    # HEAD meathod         
    else:
        path = os.getcwd() + header.get('path')
        if os.path.exists(path):
            if os.path.isdir(path):
                http_response.content_length = utf8len(render_html(path, header.get('path')))
                http_response.content_type = 'text/html'
                writer.writelines(http_response.http_response_head())

            else:
                file_type = mimetypes.guess_type(header.get('path'))[0]
                http_response.content_length = utf8len(str(os.path.getsize(path)))
                http_response.content_type = file_type
                writer.writelines(http_response.http_response_head())
    await writer.drain()
    writer.close()


def render_html(path, current_path):
    message = '<html>\n'
    message += '<head><title>' + 'Index of ' + path + '</title></head>\n'
    message += '<body bgcolor = "white">\n'
    message += '<h1>' + 'Index of ' + path + '</h1>\n'
    message += '<pre>\n'
    lists = os.listdir(path)
    for element in lists:
        if os.path.isdir(path + element):
            message += '<a href=\"' + current_path + element + '/\">' + element + '/</a><br>\n'
        else:
            message += '<a href=\"' + current_path + element + '\">' + element + '</a><br>\n'
    message += '</pre>\n'
    message += '</body>\n'
    message += '</html>'
    return str.encode(message)


if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    coro = asyncio.start_server(dispatch, '127.0.0.1', 8080, loop=loop)
    server = loop.run_until_complete(coro)

    # Serve requests until Ctrl+C is pressed
    print('Serving on {}'.format(server.sockets[0].getsockname()))

    try:
        loop.run_forever()
    except KeyboardInterrupt:
        pass

    # Close the server
    server.close()
    loop.run_until_complete(server.wait_closed())
    loop.close()
