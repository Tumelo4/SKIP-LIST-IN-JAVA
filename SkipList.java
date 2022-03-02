import java.util.Random;

@SuppressWarnings({"unchecked","rawtypes"})
public class SkipList<T extends Comparable<? super T>> {

	public int maxLevel;
	public SkipListNode<T>[] root;
	private int[] powers;
	private Random rd = new Random();

	SkipList(int i) {
		maxLevel = i;
		root = new SkipListNode[maxLevel];
		powers = new int[maxLevel];
		for (int j = 0; j < i; j++)
			root[j] = null;
		choosePowers();
		rd.setSeed(202003); // do not modify this line
	}

	SkipList() {
		this(4);
	}

	public void choosePowers() {
		powers[maxLevel - 1] = (2 << (maxLevel - 1)) - 1;
		for (int i = maxLevel - 2, j = 0; i >= 0; i--, j++)
			powers[i] = powers[i + 1] - (2 << j);
	}

	public int chooseLevel() {
		int i, r = Math.abs(rd.nextInt()) % powers[maxLevel - 1] + 1;
		for (i = 1; i < maxLevel; i++) {
			if (r < powers[i])
				return i - 1;
		}
		return i - 1;
	}

	public boolean isEmpty() {
		// your code goes here
		return root[0] == null;
	}

	public void insert(T key) {
		// your code goes here
		SkipListNode<T>[] curr = new SkipListNode[maxLevel];
		SkipListNode<T>[] prev = new SkipListNode[maxLevel];
		SkipListNode<T> newNode;

		int level, i;
		curr[maxLevel-1] = root[maxLevel-1];
		prev[maxLevel-1] = null;

		for(level = maxLevel-1; level >=0; level--){
			while( curr[level] != null && curr[level].key.compareTo(key) < 0) {
				prev[level] = curr[level];
				curr[level] = curr[level].next[level];

			}

			if(curr[level] != null && curr[level].key.equals(key))
				return;

			if(level>0)
				if(prev[level] == null) {
					curr[level-1] = root[level-1];
					prev[level-1] = null;
				}
			else {
				curr[level-1] = prev[level].next[level-1];
				prev[level-1] = prev[level];
			}
		}

			level = chooseLevel();
			newNode = new SkipListNode<T> (key, level+1);

			for(i=0; i<= level; i++) {
				newNode.next[i] = curr[i];
				if(prev[i] == null)
					root[i] = newNode;
				else prev[i].next[i] = newNode;
			}
	}

	public boolean delete(T key) {
		// your code goes here

		if(search(key) == null)
			return false;
		
		for ( int index = 0; index < maxLevel; index++)
		{
			SkipListNode<T> curr = root[index];
			SkipListNode<T> prev = null;
			while ( curr != null && curr.key.compareTo(key) != 0)
			{
				prev = curr;
				curr = curr.next[index];
			}
			
			if ( prev == null)
			{
				if (curr != null)
					root[index] = curr.next[index];
				else
				{
					root[index] = curr;
				}
			}
			else
			{
				if ( curr != null)
					prev.next[index] = curr.next[index];
				else
				{
					prev.next[index] = curr;
				}
			}
		}

		return true;
	}

	public T first() {
		// your code goes here
		return root[0].key;
	}

	public T search(T key) {


		for (int index = maxLevel-1; index>=0; index--)
		{
			SkipListNode<T> curr= root[index];

			while(curr != null)
			{
				if(curr.key.equals(key))
				{
					return key;
				}
				curr = curr.next[index];
			}
		}
		return null;
	}
	
	public String getPathToLastNode() {
		// your code goes here
		String path = "";
		// SkipListNode<T> node = null;

		// for ( int level = maxLevel -1; level >= 0 ; level--)
		// {
		// 	SkipListNode<T> curr = root[level];
	
		// 	while(curr!= null)
		// 	{
		// 		if (node == null || (node != null && node.key.compareTo(curr.key)<0 && node.key.compareTo(curr.key) != 0))
		// 		{
		// 			SkipListNode<T> currNode = curr;
		// 			while (currNode != null)
		// 			{
		// 				path +="["+currNode.key+"]";
		// 				node = currNode;
		// 				currNode = currNode.next[level];
		// 			}
		// 		}
		// 		curr = curr.next[level];
		// 	}


		// }




		int count = maxLevel -1;
		SkipListNode<T> curr = root[count];
		T tmp = null;

		while (true)
		{
			if ( curr== null)
			{
				if (count == 0) return path;
				curr = root[--count];
			}
			else
			{
				while (curr.next[count] != null)
				{
					if ( tmp == null || tmp.compareTo(curr.key) < 0){
						path +="["+curr.key+"]";
						tmp = curr.key;
					}
					curr = curr.next[count];
				}
				if ( count >= 0)
				{

					if ( tmp == null || tmp.compareTo(curr.key) < 0){
						path +="["+curr.key+"]";
						tmp = curr.key;
					}
					if (count == 0 ) return path;
					curr = curr.next[--count];
				}
				else
				{
					return path;
				}
			}

		}

	}

}
