import asyncio
import socket

async def Echo(reader, writer):
    while True:
        data = await reader.read(2048)  # 最多读取2048字节数据。如果未设置，或被设置为-1，则读取至EOF标志，并返回读到的所有字节。
        message = data.decode()
        address = writer.get_extra_info('peername') # 访问可选的传输信息。
        if data and data != b'exit':
            writer.write(data)   # 向数据流中写入数据。
            print('{} sent: {}'.format(address, message))
        else:
            return
    await writer.drain()    # 等待恢复数据写入的时机。
    writer.close()  # 关闭数据流。

if __name__ == "__main__":
    loop = asyncio.get_event_loop() # 新建当前线程缺省的 loop
    coro = asyncio.start_server(Echo, '127.0.0.1', '8080', loop=loop)   # 新建线程
    server = loop.run_until_complete(coro)  # 把协程对象交给 loop.run_until_complete，协程对象随后会在 loop 里得到运行。
    # Serve requests until Ctrl+C is pressed
    print('Serving on {}'.format(server.sockets[0].getsockname()))
    try:
        loop.run_forever()
    except KeyboardInterrupt:   # Ctrl+C
        pass
    # Close the server
    server.close()  # 关闭服务
    loop.run_until_complete(server.wait_closed())   # 保持等待，直到数据流关闭。保持等待，直到底层连接被关闭，应该在close()后调用此方法。
    loop.close()
