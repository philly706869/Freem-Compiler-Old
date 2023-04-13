package net.loute.freem.compiler.util.collection.tree

interface BinaryTree<T>: Tree<T, BinaryTreeNode<T>>, BinaryTreeNode<T> {

}
interface MutableBinaryTree<T>: BinaryTree<T>, MutableBinaryTreeNode<T> {

}

interface BinaryTreeNode<T> {
    val left: T
    val right: T
}
interface MutableBinaryTreeNode<T>: BinaryTreeNode<T> {
    override var left: T
    override var right: T
}

private class ImplementedBinaryTreeNode<T>: ImplementedBinaryTree<T>() {

}

open class ImplementedBinaryTree<T>: MutableBinaryTree<T> {
    override val height: Int
        get() = TODO("Not yet implemented")
    override val root: Tree<T, BinaryTreeNode<T>>
        get() = TODO("Not yet implemented")
    override val parent: TreeNode<T, BinaryTreeNode<T>>
        get() = TODO("Not yet implemented")
    override val children: BinaryTreeNode<T>
        get() = TODO("Not yet implemented")
    override val value: T
        get() = TODO("Not yet implemented")
    override val depth: Int
        get() = TODO("Not yet implemented")

    override fun isRoot(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLeaf(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isInternal(): Boolean {
        TODO("Not yet implemented")
    }

    override var left: T
        get() = TODO("Not yet implemented")
        set(value) {}
    override var right: T
        get() = TODO("Not yet implemented")
        set(value) {}

}