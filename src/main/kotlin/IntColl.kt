//*********************************************************************
// FILE NAME    : Intcoll.kt
// DESCRIPTION  : This file contains the class Intcoll.
// AUTHOR       : Wesly Jason
//*********************************************************************
class Intcoll {
    private var c: ListNode? = null
    private var how_many: Int

    init {
        c?.link = null
        how_many = 0
    }

    // copy the contents of obj instance to this instance
    fun copy(obj: Intcoll) {
        if (this !== obj) {
            how_many = obj.how_many
            if (obj.c == null) c = null else {
                c = ListNode(obj.c!!.info, null)
                var p = c
                var q = obj.c
                while (q!!.link != null) {
                    q = q.link
                    p!!.link = ListNode(q!!.info, null)
                    p = p.link
                }
            }
        }
    }

    // if i is greater than zero and it's not in the collection insert it
    fun insert(i: Int) {
        if (i > 0) {
            var p = c // point p at first slot that c points to
            while (p != null && p.info != i) p = p.link
            if (p == null) {
                p = ListNode(i, c)
                c = p
                how_many++
            }
        }
    }

    // if i exists in the collection and i is greater than zero, remove it
    fun omit(i: Int) {
        var p = c
        var pred: ListNode? = null
        while (p != null && p.info != i) {
            pred = p
            p = p.link
        }
        if (p != null) {
            if (pred != null) pred.link = p.link else c = p.link
            how_many--
        }
    }

    // Return true if i is in the collection, false otherwise. (i has to be greater
    // than zero)
    fun belongs(i: Int): Boolean {
        var p = c
        while (p != null && p.info != i) p = p.link
        return p != null
    }

    // return the number of elements in the collection
    fun get_howmany(): Int {
        return how_many
    }

    // print the contents of each collection
    fun print() {
        var p = c
        println()
        while (p != null) {
            println(p.info)
            p = p.link
        }
    }

    // return true if both instances of a collection contain identical collection
    fun equals(obj: Intcoll): Boolean {
        var p = c
        var result = how_many == obj.how_many
        while (p != null && result) {
            result = obj.belongs(p.info)
            p = p.link
        }
        return result
    }

    // inner class that allow for the creation of nodes within the collection
    private inner class ListNode(i: Int, p: ListNode?) {
        // initialize info to i (a value), and the link to p (pointer to i)
        var info: Int = i
        var link: ListNode? = p
    } // end of inner class ListNode
}