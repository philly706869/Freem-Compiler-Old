package net.loute.freem.compiler.util.collection.tree

interface Tree<T, C>: TreeNode<T, C> {
    val height: Int
}
interface MutableTree<T, C>: Tree<T, C>, MutableTreeNode<T, C>

interface TreeNode<T, C> {
    val root: Tree<T, C>
    val parent: TreeNode<T, C>
    val children: C
    val value: T
    val depth: Int

    fun isRoot(): Boolean
    fun isLeaf(): Boolean
    fun isInternal(): Boolean
}
interface MutableTreeNode<T, C>: TreeNode<T, C> {
    override val root: MutableTree<T, C>
    override val parent: MutableTreeNode<T, C>
    override val children: C
    override var value: T
}