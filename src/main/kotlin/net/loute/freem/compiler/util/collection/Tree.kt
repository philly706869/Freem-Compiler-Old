package net.loute.freem.compiler.util.collection

interface Tree<T> {

}
interface MutableTree<T> {

}

interface TreeNode<T> {
    val children: Map<T, TreeNode<T>>
}
private interface MutableTreeNode<T> {

}

class HashTree {

}