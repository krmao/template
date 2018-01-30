import Foundation


class CXPreferencesUtil {


    static func getBool(name: String) -> Bool {
        return UserDefaults.standard.bool(forKey: name)
    }

    static func putBool(name: String, value: Bool) {
        UserDefaults.standard.set(value, forKey: name)
    }

    static func putString(key: String, value: String) {
        UserDefaults.standard.set(value, forKey: name)
    }

    static func putJsonString(key: String, value: Any) {
        UserDefaults.standard.set(CXJsonUtil.toJSONString(value), forKey: name)
    }

    static func getString(_ key: String, _ defaultValue: String? = null) -> String? {
        return UserDefaults.standard.string(forKey: name) ?? defaultValue
    }

    static func putInt(key: String, value: Int) {
        UserDefaults.standard.set(value, forKey: name)
    }

    static func getEntity<T: Decodable>(key: String, _ type: T.Type) -> T? {
        return CXJsonUtil.parse(type, getString(key))
    }

    static func putList(_ key: String, _list: Array<Any>) {
        putJsonString(key: key, value: list)
    }


    static func putMap(key: String, map: Map<String, Any>) {
        putJsonString(key: key, value: map)
    }

    static func getMap<T: Any>(_ key: String, _ type: T.Type) -> MutableMap<String, T> {
        return getEntity(key, type)
    }

    static func getList<T: Any>(_ key: String, _ type: T.Type) -> MutableList<T> {
        return getEntity(key, type)
    }

    static func getInt(_ key: String) -> Int {
        return UserDefaults.standard.integer(forKey: key)
    }

    static func remove(_ key: String) {
        UserDefaults.standard.removeObject(forKey: key)
    }

}
