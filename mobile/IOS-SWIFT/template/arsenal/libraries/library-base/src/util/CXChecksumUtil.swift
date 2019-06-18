import Foundation

class CXChecksumUtil {

    public static func genMD5Checksum(_ string: String) -> String {
        let str = string.cString(using: String.Encoding.utf8)
        let strLen = CUnsignedInt(string.lengthOfBytes(using: String.Encoding.utf8))
        let digestLen = Int(CC_MD5_DIGEST_LENGTH)
        let result = UnsafeMutablePointer<CUnsignedChar>.allocate(capacity: digestLen)
        CC_MD5(str!, strLen, result)
        let hash = NSMutableString()
        for i in 0..<digestLen {
            hash.appendFormat("%02x", result[i])
        }
        result.deinitialize()

        return String(format: hash as String)
    }

    public static func genMD5Checksum(_ fromFile: File?) -> String? {
        if (fromFile != nil) {

            let bufferSize = 1024 * 1024

            do {
                // Open file for reading:
                let file = try FileHandle(forReadingAtPath: fromFile!.path)
                if (file != nil) {
                    defer {
                        file!.closeFile()
                    }

                    // Create and initialize MD5 context:
                    var context = CC_MD5_CTX()
                    CC_MD5_Init(&context)

                    // Read up to `bufferSize` bytes, until EOF is reached, and update MD5 context:
                    while autoreleasepool(invoking: {
                        let data = file!.readData(ofLength: bufferSize)
                        if data.count > 0 {
                            data.withUnsafeBytes {
                                _ = CC_MD5_Update(&context, $0, numericCast(data.count))
                            }
                            return true // Continue
                        } else {
                            return false // End of file
                        }
                    }) {
                    }

                    // Compute the MD5 digest:
                    var digest: Data = Data(count: Int(CC_MD5_DIGEST_LENGTH))
                    digest.withUnsafeMutableBytes {
                        _ = CC_MD5_Final($0, &context)
                    }

                    let hexDigest: String = digest.map {
                        String(format: "%02hhx", $0)
                    }.joined()

                    return hexDigest
                }

            } catch {
                CXLogUtil.e("cannot open file:\(error)")
            }
        }
        return nil
    }
}
