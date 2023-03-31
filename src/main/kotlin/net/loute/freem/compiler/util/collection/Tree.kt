package net.loute.freem.compiler.util.collection

interface Tree<T>: TreeNode<T>, Collection<T> {

}
interface MutableTree<T>: TreeNode<T>, MutableCollection< T> {

}

interface TreeNode<T> {
    val parent: TreeNode<T>?
    val children: Map<T, TreeNode<T>>?
}
private interface MutableTreeNode<T>: TreeNode<T> {
    override val parent: MutableTreeNode<T>?
    override val children: MutableMap<T, MutableTreeNode< T>>?
}

open class HashTree<T>: MutableTree<T> {
    override val parent: TreeNode<T>?
        get() = TODO("Not yet implemented")
    override val children: Map<T, TreeNode<T>>?
        get() = TODO("Not yet implemented")
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun addAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun add(element: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<T> {
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

    override fun containsAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(element: T): Boolean {
        TODO("Not yet implemented")
    }

}