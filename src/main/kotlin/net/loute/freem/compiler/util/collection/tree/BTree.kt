package net.loute.freem.compiler.util.collection.tree

interface BTree<T>: Tree<T, List<BTreeNode<T>>>, BTreeNode<T>
interface MutableBTree<T>: BTree<T>, MutableTree<T, List<BTreeNode<T>>>, MutableBTreeNode<T>

interface BTreeNode<T>: TreeNode<T, List<BTreeNode<T>>>, Collection<T>
interface MutableBTreeNode<T>: BTreeNode<T>, MutableTreeNode<T, List<BTreeNode<T>>>, MutableCollection<T> {
    override val children: List<MutableBTreeNode<T>>
}

private class ImplementedBTreeNode<T>: MutableBTreeNode<T>, ImplementedBTree<T>() {

}

open class ImplementedBTree<T>: MutableBTree<T> {
    override val height: Int
        get() = TODO("Not yet implemented")
    override val root: MutableTree<T, List<BTreeNode<T>>>
        get() = TODO("Not yet implemented")
    override val parent: MutableTreeNode<T, List<BTreeNode<T>>>
        get() = TODO("Not yet implemented")
    override val children: List<MutableBTreeNode<T>>
        get() = TODO("Not yet implemented")
    override var value: T
        get() = TODO("Not yet implemented")
        set(value) {}
    override val depth: Int
        get() = TODO("Not yet implemented")

    override fun isRoot(): Boolean = true
    override fun isLeaf(): Boolean = TODO("Not yet implemented")
    override fun isInternal(): Boolean = false

    override val size: Int get() = innerSize
    private val innerSize: Int = 0

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<T> {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun remove(element: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun addAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun add(element: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(element: T): Boolean {
        TODO("Not yet implemented")
    }

}