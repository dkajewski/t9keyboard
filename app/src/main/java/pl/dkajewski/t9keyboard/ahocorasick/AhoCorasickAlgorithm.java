package pl.dkajewski.t9keyboard.ahocorasick;

import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * I don't know if I'm gonna use this algorithm in the project, but it's nice to have
 * Plans for next updates are:
 * - add dictionary parsing methods for T9 input method (i.e. writing word "smart" will be realized by pressing
 * 76278, so in db we can try to SELECT * FROM words WHERE t9 LIKE '76278%' ORDER BY weight DESC;
 * performance tests needed; other option is to create a gigantic dictionary trie using this algorithm implementation
 * but I wasn't thinking about word weight updating, maybe I would add this data to db, but again:
 * it could be very time consuming :/)
 *
 * The Aho-Corasick algorithm implementation
 * Example of usage:
 *   AhoCorasickAlgorithm ahoCorasick = new AhoCorasickAlgorithm();
 *   String main = "word";
 *   String pattern = "word";
 *   ahoCorasick.addString(pattern);
 *   int node = 0;
 *   for (char ch : main.toCharArray()) {
 *      node = ahoCorasick.transition(node, ch);
 *   }
 *
 *   if (ahoCorasick.nodes.get(node).leaf) {
 *      Log.d("aho", "Pattern: "+pattern+" is in the dictionary: "+main);
 *   }  else {
 *      Log.d("aho", "Pattern: "+pattern+" is not in the dictionary: "+main);
 *   }
 */
public class AhoCorasickAlgorithm
    {

        public SparseArray<Node> nodes = new SparseArray<>();
        public int nodeCount;

        public static class Node
        {
            public int parent;
            public char charFromParent;
            public int suffixLink = -1;
            public SparseIntArray children = new SparseIntArray();
            public SparseIntArray transitions = new SparseIntArray();
            public boolean leaf;
        }

        public AhoCorasickAlgorithm()
        {
            Node root = new Node();
            root.suffixLink = 0;
            root.parent = -1;
            this.nodeCount = 1;
            this.nodes.append(0, root);
        }

        /**
         * Method adds word to dictionary
         * @param word word to add
         */
        public void addString(String word)
        {
            int current = 0;
            for (char letter : word.toCharArray()) {
                if (this.nodes.get(current).children.get(letter, -1) == -1) {
                    Node node = new Node();
                    node.parent = current;
                    node.charFromParent = letter;
                    this.nodes.append(this.nodeCount, node);
                    this.nodes.get(current).children.append(letter, this.nodeCount++);

                }

                current = this.nodes.get(current).children.get(letter);
            }

            this.nodes.get(current).leaf = true;
        }

        /**
         * Method for finding max suffix link
         * @param nodeIndex index of node from this.nodes
         * @return index of node
         */
        public int suffixLink(int nodeIndex)
        {
            Node node = this.nodes.get(nodeIndex);
            if (node.suffixLink == -1) {
                if (node.parent == 0) {
                    node.suffixLink = 0;
                } else {
                    node.suffixLink = transition(suffixLink(node.parent), node.charFromParent);
                }
            }

            return node.suffixLink;
        }

        /**
         * Method for searching next state
         * @param nodeIndex current node index
         * @param ch searched character
         * @return node index of searched character
         */
        public int transition(int nodeIndex, char ch)
        {
            Node node = nodes.get(nodeIndex);
            if (node.transitions.get(ch, -1) == -1) {
                if (node.children.get(ch, -1) != -1) {
                    node.transitions.append(ch, node.children.get(ch));
                } else {
                    node.transitions.append(ch, (nodeIndex == 0 ? 0 : transition(suffixLink(nodeIndex), ch)));
                }
            }

            return node.transitions.get(ch);
        }
}

