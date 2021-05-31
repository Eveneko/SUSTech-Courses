# Report 9

Assume, for all of the questions in this part, that filesystem blocks are 4 KBytes.

a.  Consider a really simple filesystem, cs302fs, where each inode only has 10 direct pointers, each of which can point to a single file block. Direct pointers are 32 bits in size (4 bytes). What is the maximum file size for cs302fs?

    block number =  10
    Maximum File size = 10 * 4 KB = 40KB

b.  Consider a filesystem, called extcs302fs, with a construct called an extent. Extents have a pointer (base address) and a length (in blocks). Assume the length field is 8 bits (1 byte). Assuming that an inode has exactly one extent. What is the maximum file size for extcs302fs?

    Maximun Length = 2^8 - 1 = 255
    Maximun Fize Size = 255 * 4KB = 1020KB

Consider a filesystem that uses direct pointers, but also adds indirect pointers and double-indirect pointers. We call this filesystem, indcs302fs. Specifically, an inode within indcs302fs has 1 direct pointer, l indirect pointer, and 1 doubly-indirect pointer field. Pointers, as before, are 4 bytes (32 bits) in size. What is the maximum file size for indcs302fs?

    Maximun Fize Size = 4KB + (4KB / 4B) * 4B + (4KB / 4B)^2 * 4KB = 4198404KB = 4100MB
