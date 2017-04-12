/**
 * A class representing a node in a BST.
 */
class Node{
    private Node leftChild;
    private Node rightChild;
    private Node parent;
    private Record record;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public int compareTo(Node other){
        return other.getRecord().getKey().compareTo(record.getKey());
    }
}



public class OrderedDictionary implements OrderedDictionaryADT {

    private Node root;

    public OrderedDictionary() {
        root = new Node();
        initRoot();
    }

    /**
     * Initializes the root with two leaves.
     */
    private void initRoot(){
        root.setLeftChild(new Node());
        root.getLeftChild().setParent(root);
        root.setRightChild(new Node());
        root.getRightChild().setParent(root);
    }


    /**
     * Recursively searches the tree for a node containing a specified key.
     *
     * @param n the node we are currently checking if it contains key k
     * @param k the key we are searching for
     * @return the Node with a key matching k if it exists in the tree, otherwise returns the leaf where
     * the key k would be expected.
     */
    private Node treeSearch(Node n, Key k) {

        // Base case, if we have reached a leaf then return.
        if (n.getRecord() == null) {
            return n;
        }

        // Using the comparison to determine which direction to travel in.
        int comparison = k.compareTo(n.getRecord().getKey());

        // If we have found a match return the node, otherwise go int he appropriate direction.
        if (comparison == 0) {
            return n;
        } else if (comparison == 1) {
            return treeSearch(n.getLeftChild(), k);
        } else {
            return (treeSearch(n.getRightChild(), k));
        }
    }

    /**
     * Returns the Record object with key k, or it returns null if such a record is not in the dictionary.
     */
    public Record find(Key k) {
        return treeSearch(root, k).getRecord();
    }

    /**
     * Inserts r into the ordered dictionary.
     * It throws a DictionaryException if a record with the same key as r is already in the dictionary.
     */
    public void insert(Record r) throws DictionaryException {

        // If this is the very first insert, we only deal with the root.
        if (root.getRecord() == null) {
            root.setRecord(r);
        }

        else {

            // We use treeSearch to find the position where the new record should go.
            Node position = treeSearch(root, r.getKey());

            // If the returned position already has a record, this means that this record is already in the dictionary.
            if (position.getRecord() != null) {
                throw new DictionaryException("Record already contained in the dictionary.");
            }

            // Otherwise we set the value of the leaf to the record and initialize it's two children leaves.
            else {
                position.setRecord(r);
                position.setLeftChild(new Node());
                position.getLeftChild().setParent(position);
                position.setRightChild(new Node());
                position.getRightChild().setParent(position);
            }
        }
    }

    /**
     * Removes an external node from the dictionary.
     *
     * @param n an external node to be removed from the dictionary
     */
    private void removeExternal(Node n) {

        Node parent = n.getParent();

        // If we are trying to remove the root and it is an external node,
        // than one of it's children must become the new root.
        if (parent == null){

            // If the root has no left child than we set the new root as the right child (may potentially be a leaf)
            if (n.getLeftChild().getRecord() == null){
                root = n.getRightChild();
                root.setParent(null);
            }

            // Otherwise we know that the left child is not a leaf, therefore it becomes the root.
            else {
                root = n.getLeftChild();
                root.setParent(null);
            }

            // If we initialized the root to a leaf, than it needs to be initialized.
            if (root.getRecord() == null){
                initRoot();
            }
        }

        else {

            // If the node that we wish to remove is a left child...
            if (parent.getLeftChild() == n) {

                // If the record of the left is null than that means that either the record of the right is not null
                // (in which case we need to make the parent point to this node now),
                // or that the record of the right child is null, therefore it is a leaf,
                // (in which case we still need to make it the child of the parent).
                if (n.getLeftChild().getRecord() == null) {
                    parent.setLeftChild(n.getRightChild());
                }

                // If the left child does not have a null record than this means that the right child has a null record,
                // therefore we set the left child as the child of the parent.
                else {
                    parent.setLeftChild(n.getLeftChild());
                }
            }

            // We follow the same logic for if the node we wish to remove is itself a right child...
            else {

                if (n.getLeftChild().getRecord() == null) {
                    parent.setRightChild(n.getRightChild());
                } else {
                    parent.setRightChild(n.getLeftChild());
                }
            }
        }
    }

    /**
     * Removes an internal node from the dictionary.
     *
     * @param n an internal node to be removed from the dictionary
     */
    private void removeInternal(Node n){

        // We find the next greatest node.
        // We know that n has a right child since it is an internal node.
        Node nextGreatest = n.getRightChild();
        while (nextGreatest.getLeftChild().getRecord() != null){
            nextGreatest = nextGreatest.getLeftChild();
        }

        // We preserve the value of the nextGreatest by setting n's value as it.
        n.setRecord(nextGreatest.getRecord());

        // We then remove nextGreatest since it will be definition be an external node.
        removeExternal(nextGreatest);
    }


    /**
     * Removes the record with Key k from the dictionary.
     * It throws a DictionaryException if the record is not in the dictionary.
     */
    public void remove(Key k) throws DictionaryException {
        Node position = treeSearch(root, k);

        // If we cannot find the key, then it is not in the dictionary.
        if (position.getRecord() == null){
            throw new DictionaryException("Key is not contained in the dictionary.");
        }
        else{

            // We determine if the node is external or internal based on it's children, and remove accordingly.
            if (position.getLeftChild().getRecord() == null || position.getRightChild().getRecord() == null){
                removeExternal(position);
            }
            else{
                removeInternal(position);
            }
        }
    }

    /** Returns the successor of k (the record from the ordered dictionary with smallest key larger than k);
     * it returns null if the given key has no successor.
     * The given key DOES NOT need to be in the dictionary.
     */
    public Record successor(Key k) {

        // We get the node containing the key,
        // or the spot it should be to deal with if the node is not in the dictionary.
        Node current = treeSearch(root, k);

        // If it contains a right child than we use this to determine the successor.
        // Use a short-circuit operator to deal with the situation when we have a leaf node.
        if (current.getRightChild() != null && current.getRightChild().getRecord() != null) {
            current = current.getRightChild();
            while (current.getLeftChild().getRecord() != null) {
                current = current.getLeftChild();
            }
            return current.getRecord();
        }

        // Otherwise we go up towards the root.
        else {
            Node ancestor = current.getParent();

            // If we go past the root without finding an successor than one does not exist.
            while (ancestor != null){
                if (k.compareTo(ancestor.getRecord().getKey()) == 1){
                    return ancestor.getRecord();
                }
                ancestor = ancestor.getParent();
            }
            return null;
        }
    }


    /**
     * Returns the predecessor of k (the record from the ordered dictionary with largest key smaller than k;
     * it returns null if the given key has no predecessor.
     * The given key DOES NOT need to be in the dictionary.
     */
    public Record predecessor(Key k) {

        // We get the node containing the key,
        // or the spot it should be to deal with if the node is not in the dictionary.
        Node current = treeSearch(root, k);

        // If it contains a left child than we use this to determine the predecessor.'
        // Use a short-circuit operator to deal with the situation when we have a leaf node.
        if (current.getLeftChild() != null && current.getLeftChild().getRecord() != null) {
            current = current.getLeftChild();
            while (current.getRightChild().getRecord() != null) {
                current = current.getRightChild();
            }
            return current.getRecord();
        }

        // Otherwise we go up towards the root.
        else {
            Node ancestor = current.getParent();

            // If we go past the root without finding an predecessor than one does not exist.
            while (ancestor != null){
                if (k.compareTo(ancestor.getRecord().getKey()) == -1){
                    return ancestor.getRecord();
                }
                ancestor = ancestor.getParent();
            }

            return null;
        }
    }

    /**
     * Returns the record with smallest key in the ordered dictionary. Returns null if the dictionary is empty.
     */
    public Record smallest() {
        Node current = root;

        // If the root is empty, than so is the rest of the tree, therefore we return null.
        if (current.getRecord() == null){
            return null;
        }
        else {

            // We go as far to the left as we can to get the smallest element.
            while (current.getLeftChild().getRecord() != null){
                current = current.getLeftChild();
            }
            return current.getRecord();
        }
    }

    /**
     * Returns the record with largest key in the ordered dictionary. Returns null if the dictionary is empty.
     */
    public Record largest() {
        Node current = root;

        // If the root is empty, than so is the rest of the tree, therefore we return null.
        if (current.getRecord() == null){
            return null;
        }
        else {

            // We go as far to the right as we can to get the largest element.
            while (current.getRightChild().getRecord() != null){
                current = current.getRightChild();
            }
            return current.getRecord();
        }
    }
}
