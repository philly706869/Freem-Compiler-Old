package net.loute.freem.compiler.util

//class ElementDuplicateException(override val message: String?): Exception(message)
//
//interface IndexMap <K>: Map<K, Int>
//
//interface IndexMapConstructor <K> { fun commit(vararg item: K) }
//
//fun <K> indexMapOf(initialIndex: Int = 0, pushRule: (index: Int) -> Int = { it + 1 }, constructor: IndexMapConstructor<K>.() -> Unit): IndexMap<K> {
//    val indexMapConstructor = object: IndexMapConstructor<K> {
//        val entries = mutableMapOf<K, Int>()
//        private var index = initialIndex
//        override fun commit(vararg items: K) {
//            items.forEach {
//                if (entries.any { index -> index.key == items }) throw ElementDuplicateException("element duplicated: $it")
//                entries[it] = index
//            }
//            index = pushRule(index)
//        }
//    }.apply(constructor)
//    val map = indexMapConstructor.entries.toMap()
//    return object: IndexMap <K> {
//        override val entries: Set<Map.Entry<K, Int>> = map.entries
//        override val keys: Set<K> = map.keys
//        override val size: Int = map.size
//        override val values: Collection<Int> = map.values
//        override fun isEmpty(): Boolean = entries.isEmpty()
//        override fun get(key: K): Int? = entries.find { it.key == key }?.value
//        override fun containsValue(value: Int): Boolean = values.contains(value)
//        override fun containsKey(key: K): Boolean = keys.contains(key)
//    }
//}
