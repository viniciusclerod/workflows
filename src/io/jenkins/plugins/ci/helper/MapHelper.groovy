package io.jenkins.plugins.ci.helper

class MapHelper {
    static Map merge(Map map1, Map map2, Closure cls = { a, b -> a + b }) {
        (map1.keySet() + map2.keySet()).collectEntries {
            [(it): map1[it] && map2[it] ? cls.call(map1[it], map2[it]) : (map1[it] ?: map2[it])]
        }
    }
}
