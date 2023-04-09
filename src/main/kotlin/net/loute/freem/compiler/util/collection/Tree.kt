package net.loute.freem.compiler.util.collection

interface Tree<T, C>: TreeNode<T, C> {
    val height: Int
    val children: C
}

interface MutableTree<T, C>: Tree<T, C>, MutableCollection<T> {

}

interface TreeNode<T, C>: Collection<T> {
    val root: Tree<T, C>
    val parent: TreeNode<T, C>
    val depth: Int

    fun isRoot(): Boolean
    fun isLeaf(): Boolean
    fun isInternal(): Boolean
}
interface MutableTreeNode<T, C>: TreeNode<T, C>, MutableCollection<T> {
    override val root: MutableTree<T, C>
    override val parent: MutableTreeNode<T, C>
}

interface BTree<T>: BTreeNode<T> {

}
interface MutableBTree<T>: BTree<T>, MutableTree<T, List<BTreeNode<T>>> {

}

interface BTreeNode<T>: TreeNode<T, List<BTreeNode<T>>> {

}
interface MutableBTreeNode<T>: BTreeNode<T>, MutableTreeNode<T, List<MutableBTreeNode<T>>> {

}

interface BinaryTree {

}
interface MutableBinaryTree {

}

interface BinaryTreeNode {

}
interface MutableBinaryTreeNode {

}

interface BinaryTreeChildren<T> {
    val left: T
    val right: T
}
interface MutableBinaryTreeChildren<T>: BinaryTreeChildren<T> {
    override val left: T
    override val right: T
}
