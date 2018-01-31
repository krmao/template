import Foundation


class CXPreferencesUtil {


    static func getBool(_ key: String) -> Bool {
        return UserDefaults.standard.bool(forKey: key)
    }

    static func putBool(_ key: String, _ value: Bool) {
        UserDefaults.standard.set(value, forKey: key)
    }

    static func putString(_ key: String, _ value: String) {
        UserDefaults.standard.set(value, forKey: key)
    }

    static func putJsonString<T: Encodable>(_ key: String, _ value: T) {
        UserDefaults.standard.set(CXJsonUtil.toJSONString(value), forKey: key)
    }

    static func getString(_ key: String, _ defaultValue: String? = nil) -> String? {
        return UserDefaults.standard.string(forKey: key) ?? defaultValue
    }

    static func putInt(_ key: String, _ value: Int) {
        UserDefaults.standard.set(value, forKey: key)
    }

    static func getEntity<T: Decodable>(_ key: String, _ type: T.Type) -> T? {
        return CXJsonUtil.parse(type, getString(key))
    }

    static func putList<T: Encodable>(_ key: String, _ list: Array<T>) {
        putJsonString(key, list)
    }


    static func putMap<T: Encodable>(_ key: String, _ map: Map<String, T>) {
        putJsonString(key, map)
    }

    static func getMap<T: Decodable>(_ key: String, _ type: T.Type) -> MutableMap<String, T>? {
//        return getEntity(key, Dictionary<String, T>)
        return MutableMap<String, T>()
    }

    static func getList<T: Decodable>(_ key: String, _ type: T.Type) -> MutableList<T>? {
//        return getEntity(key, Array<T>)
        return MutableList<T>()
    }

    static func getInt(_ key: String) -> Int {
        return UserDefaults.standard.integer(forKey: key)
    }

    static func remove(_ key: String) {
        UserDefaults.standard.removeObject(forKey: key)
    }

}
