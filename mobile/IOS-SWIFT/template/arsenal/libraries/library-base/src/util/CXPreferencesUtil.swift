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
        let json = CXJsonUtil.toJSONString(value)
        //CXLogUtil.e("preferences:putJsonString:json->\(json)")
        UserDefaults.standard.set(json, forKey: key)
    }

    static func getString(_ key: String, _ defaultValue: String? = nil) -> String? {
        return UserDefaults.standard.string(forKey: key) ?? defaultValue
    }

    static func putInt(_ key: String, _ value: Int) {
        UserDefaults.standard.set(value, forKey: key)
    }

    static func getEntity<T: Decodable>(_ key: String, _ type: T.Type) -> T? {
        let json = getString(key)
        //CXLogUtil.e("preferences:getEntity:json->\(json)")
        let entity: T? = CXJsonUtil.parse(json)
        //CXLogUtil.e("preferences:getEntity:entity->\(entity)")
        return entity
    }

    static func putList<T: Encodable>(_ key: String, _ list: Array<T>) {
        putJsonString(key, list)
    }


    static func putMap<T: Encodable>(_ key: String, _ map: Map<String, T>) {
        putJsonString(key, map)
    }

    static func getMap<T: Decodable>(_ key: String) -> MutableMap<String, T>? {
        let json = getString(key)
        //CXLogUtil.e("preferences:getMap:json->\(json)")
        let map: MutableMap<String, T>? = CXJsonUtil.parse(json)
        //CXLogUtil.e("preferences:getMap:map->\(map)")
        return map
    }

    static func getList<T: Decodable>(_ key: String, _ type: T.Type) -> MutableList<T>? {
        let json = getString(key)
        //CXLogUtil.e("preferences:getList:json->\(json)")
        let list: MutableList<T>? = CXJsonUtil.parse(json)
        //CXLogUtil.e("preferences:getList:list->\(list)")
        return list
    }

    static func getInt(_ key: String) -> Int {
        return UserDefaults.standard.integer(forKey: key)
    }

    static func remove(_ key: String) {
        UserDefaults.standard.removeObject(forKey: key)
    }

}
