package io.jenkins.plugins.ci.helper

class MapHelper {

    static Map merge(Map map1, Map map2, Closure cls = { a, b -> a + b }) {
        (map1.keySet().plus(map2.keySet()).unique()).collectEntries {
            [(it): map1[it] && map2[it] ? cls.call(map1[it], map2[it]) : (map1[it] ?: map2[it])]
        }
    }

    static def getValueByKeys(Map map, List<String> keys) {
        if (keys.size() == 1) return map[keys[0]]
        else return MapHelper.getValueByKeys(map[keys[0]], keys[1..-1])
    }

}
