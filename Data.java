package src;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
//this class can handle resizing and collisions

/**
 * @author Nikou Kalbali
 *
 * @param <Key>
 * @param <Value>
 */
class Data<Key, Value> {
	Key key;
	Value value;

	// Reference to next node
	Data<Key, Value> next;

	// Constructor
	public Data(Key key, Value value) {
		this.key = key;
		this.value = value;
	}
}

// Class to represent entire hash table
/**
 * @author Nikou K
 *
 * @param <Key>
 * @param <Value>
 */
class Map<Key, Value> {
	// bucketArray is used to store array of chains
	private ArrayList<Data<Key, Value>> bucketArray;
	
	// Current capacity of array list
	private int numBuckets;

	// Current size of array list
	private int size;
	private int totalsize;

	// Constructor (Initializes capacity, size and
	// empty chains.
	public Map() {
		bucketArray = new ArrayList<Data<Key, Value>>();
		numBuckets = 10;
		size = 0;
		totalsize =0;

		// Create empty chains
		for (int i = 0; i < numBuckets; i++)
			bucketArray.add(null);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	// This implements hash function to find index
	// for a key
	/**
	 * @param key
	 * @return
	 */
	private int getBucketIndex(Key key) {
		int hashCode = key.hashCode();
		
		//int index = hashCode % numBuckets ;
		int index = Math.floorMod (hashCode, numBuckets) ;
		return index;
	}

	// Method to remove a given key
	public Value remove(Key key) {
		// Apply hash function to find index for given key
		int bucketIndex = getBucketIndex(key);

		// Get head of chain
		Data<Key, Value> head = bucketArray.get(bucketIndex);

		// Search for key in its chain
		Data<Key, Value> prev = null;
		while (head != null) {
			// If Key found
			if (head.key.equals(key))
				break;

			// Else keep moving in chain
			prev = head;
			head = head.next;
		}

		// If key was not there
		if (head == null)
			return null;

		// Reduce size
		size--;

		// Remove key
		if (prev != null)
			prev.next = head.next;
		else
			bucketArray.set(bucketIndex, head.next);

		return head.value;
	}

	// Method find number of node in chain by a given key
	/**
	 * @param key
	 * @return
	 */
	public int chainSize(Key key) {
		// Apply hash function to find index for given key
		int bucketIndex = getBucketIndex(key);
		int chainSize = 0;
		// Get head of chain
		Data<Key, Value> head = bucketArray.get(bucketIndex);
		// If key was not there
		if (head == null)
			chainSize = -1; // return -1 if there is no such item

		// Search for key in its chain

		Data<Key, Value> prev = null;
		while (head != null) {

			// Else keep moving in chain
			chainSize = chainSize + 1;
			prev = head;
			head = head.next;
		}

		return chainSize;
	}

	// Method find sum of claims in chain( in a state) by a given key
	/**
	 * @param key
	 * @return
	 */
	public float claimsTot(Key key) {
		// Apply hash function to find index for given key
		int bucketIndex = getBucketIndex(key);
		float claimSum = 0;
		// Get head of chain
		Data<Key, Value> head = bucketArray.get(bucketIndex);
		// If key was not there
		if (head == null)
			claimSum = -1; // return -1 if there is no such item

		// Search for key in its chain

		Data<Key, Value> prev = null;
		while (head != null) {

			// Else keep moving in chain
			Drug myValue = (Drug) head.value;
			claimSum = claimSum + myValue.getClaims();
			prev = head;
			head = head.next;
		}
		return claimSum;
	}
	public float aggregateTot(Key key) {
		// Apply hash function to find index for given key
		int bucketIndex = getBucketIndex(key);
		float aggregateCostSum = 0;
		// Get head of chain
		Data<Key, Value> head = bucketArray.get(bucketIndex);
		// If key was not there
		if (head == null)
			aggregateCostSum = -1; // return -1 if there is no such item

		// Search for key in its chain

		Data<Key, Value> prev = null;
		while (head != null) {

			// Else keep moving in chain
			Drug myValue = (Drug) head.value;
			aggregateCostSum = aggregateCostSum + myValue.getAggregateCost();
			prev = head;
			head = head.next;
		}
		return aggregateCostSum;
	}

	// Returns value for a key
	public Value get(Key key) {
		// Find head of chain for given key
		try{
		int bucketIndex = getBucketIndex(key);
		Data<Key, Value> head = bucketArray.get(bucketIndex);

		// Search key in chain
		while (head != null) {
			if (head.key.equals(key))
				return head.value;
			head = head.next;
		}
		}catch(Exception ex){
			System.out.println("Exception- get method " + ex.getMessage());

		}
		// If key not found
		return null;
	}

	// Adds a key value pair to hash
	/**
	 * @param key
	 * @param value
	 */
	public void add(Key key, Value value) {
		size++;
		// Find head of chain for given key
		try{
		
		int bucketIndex = getBucketIndex(key);
		Data<Key, Value> head = bucketArray.get(bucketIndex);

		// Check if key is already present
		while (head != null) {
			
			head = head.next;
		} 

		// Insert key in chain
		
		head = bucketArray.get(bucketIndex);
		Data<Key, Value> newNode = new Data<Key, Value>(key, value);
		newNode.next = head;
		bucketArray.set(bucketIndex, newNode);
		}catch(Exception ex){
			System.out.println("Exception- add method " + ex.getMessage());

		}

		// If load factor goes beyond threshold, then
		// double hash table by calling resize method
		// I added divided by 200 to ensure size stay low
		if ((1.0 * size/200) / numBuckets >= 0.75) {
			resize();
		}
	}
	//resizing the map 
	private void resize(){
		ArrayList<Data<Key, Value>> temp = bucketArray;
		bucketArray = new ArrayList<Data<Key, Value>>();
		numBuckets = 2 * numBuckets;
		size = 0;
		for (int i = 0; i < numBuckets; i++)
			bucketArray.add(null);
		for (Data<Key, Value> headNode : temp) {
			
			while (headNode != null) {
				
				add(headNode.key, headNode.value);
				headNode = headNode.next;
			}
		}
	}

	// Driver method to test Map class

	public static void main(String[] args) {		
		tempLoadFile();

	}
	
	
	private static void tempLoadFile(){
		String folder = "data"; //the location of generate File
		final String hashes = folder + "\\hash.txt";
		
		Map<Integer, Drug> mapDrug = new Map<Integer, Drug>();
		Map<Integer, Drug> mapState = new Map<Integer, Drug>();
		Map<Integer, Drug> mapByName = new Map<Integer, Drug>();
		int rowNumber=0;
		String prevState = "";
		Scanner lineIn;
		Drug prevdrug =null;
		try {
			lineIn = new Scanner(new File(hashes));

			lineIn.useDelimiter("\n");
			String contents = "";
			String currentState = "";
			while (lineIn.hasNext()) {
				
				String i = lineIn.next();
				String[] info = i.split(",");
				String state = info[0].trim();
				if (!(state.equals(currentState))) {
					//System.out.println("Loading for state: " + state + "...");
					currentState = state;
				}
				String name = info[1];
				int claims = Integer.parseInt(info[2].trim());
				int aggregateCost = Integer.parseInt(info[3].trim());
				Drug drug = new Drug(state, name, claims, aggregateCost);
				rowNumber++;
				mapByName.add(drug.hashByName(),drug);
				mapDrug.add(drug.hashCode(),drug);
				mapState.add(state.hashCode(),drug);
				prevdrug = drug;
				
				
				if (!prevState.equals(drug.getState()) && !prevState.equals("") ){
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					System.out.println("Drug Map: claimsTot: " + decimalFormat.format(mapDrug.claimsTot(prevdrug.hashCode())));
					System.out.println("Drug Map: aggregateTot: " + decimalFormat.format(mapDrug.aggregateTot(prevdrug.hashCode())));
					System.out.println("Drug Map: Prev State Name : " + prevState);
					System.out.println("Drug Map: current State Name : " + prevState);
					System.out.println("Drug Map: current Column Array List Size: " + mapDrug.bucketArray.size());
					
					}
				if (!prevState.equals(state)){
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					System.out.println("State Map: claimsTot for : " +  prevState +  ": " + decimalFormat.format(mapState.claimsTot(prevState.hashCode())));
					System.out.println("State Map: aggregateTot for : "  +  prevState +  ": "+ decimalFormat.format(mapState.aggregateTot(prevState.hashCode())));
					System.out.println("State Map: Prev State Name : " + prevState);
					System.out.println("State Map: current State Name : " + state);
					System.out.println("State Map: current Column Array List Size: " + mapState.bucketArray.size());
				}
				
				prevState = drug.getState();
			}
			lineIn.close();
			DecimalFormat decimalFormat = new DecimalFormat("0.00");

			System.out.println("Drug Map: claimsTot: for ZYVOX: " + decimalFormat.format(mapByName.claimsTot("ZYVOX".hashCode())));
			System.out.println("Drug Map: aggregateTot: for ZYVOX: " + decimalFormat.format(mapByName.aggregateTot("ZYVOX".hashCode())));
			System.out.println("State Map: claimsTot for : " + "Wyoming" +  ": " + decimalFormat.format(mapState.claimsTot("Wyoming".hashCode())));
			System.out.println("State Map: aggregateTot for : "  +  "Wyoming" +  ": "+ decimalFormat.format(mapState.aggregateTot("Wyoming".hashCode())));
			
			System.out.println(" Row Counter: "+ rowNumber);
			System.out.println(" Drug Map: Map Total size End: " + mapDrug.size());
			System.out.println(" Drug Map: current Column Array List Size End: " + mapDrug.bucketArray.size());
			System.out.println(" Drug Map: current Column Array List Size End:-bucketArray " + mapDrug.size());
			System.out.println(" State Map: Map Total size End: " + mapState.size());
			System.out.println(" State Map: current Column Array List Size End: " + mapState.bucketArray.size());
			System.out.println(" State Map: current Column Array List Size End:-bucketArray " + mapState.size());
	

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}  // End of Calss
