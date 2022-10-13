/* Name: Hitesh Sah
 * NetId: hks200000
 * Course: 3345.005
 * project Description: Implementation of the hash table structure using Linear Probing Strategies
 */

public class HashTableLinearProbe<K, V> {
	
	//Default table size
    private static final int DEFAULT_TABLE_SIZE = 3;

    private HashEntry<K, V>[] hashtable;

    // constructor
    public HashTableLinearProbe() {
        hashtable = new HashEntry[DEFAULT_TABLE_SIZE];
    }

    public boolean insert(K key, V value) {
        // making sure key is not null
        if (key == null) {
            throw new IllegalArgumentException("Key is invalid");
        }
        // making sure value is not null
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }

        // create entry out of key and value
        HashEntry<K, V> entry = new HashEntry<>(key, value);
        // trying to insert
        InsertResult result = insert(entry);

        // checking if rehashing is required
        if (result == InsertResult.REHASH) {
            rehash();
            // insert entry after rehash
            result = insert(entry);
        }

        return result == InsertResult.SUCCESS;
    }

    public V find(K key) {
        // making sure key is not null
        if (key == null) {
            throw new IllegalArgumentException("invalid key");
        }

        // find the entry with given key
        HashEntry<K, V> entry = findHashEntry(key);

        // if entry is null or deleted return null, else return the entry value
        return entry == null || entry.isDeleted ? null : entry.value;
    }

    public boolean delete(K key) {
        // making sure key is not null
        if (key == null) {
            throw new IllegalArgumentException("Invalid key");
        }

        // find the entry with given key
        HashEntry<K, V> entry = findHashEntry(key);

        // if no entry with key or entry already deleted, return false
        if (entry == null || entry.isDeleted) {
            return false;
        } else { // mark entry as deleted and return true
            entry.isDeleted = true;
            return true;
        }
    }


    public int getHashValue(K key) {
        // if key is null, return -1
        if (key == null) {
            return -1;
        }
        // mod with capacity is done to get the value within length of hash table
        return key.hashCode() % hashtable.length;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{\n");
        for (HashEntry<K, V> entry : hashtable) {
            if (entry != null && !entry.isDeleted) {
                builder.append(entry.key)
                        .append('=')
                        .append(entry.value)
                        .append('\n');
            }
        }
        return builder.append('}').toString();
    }

    //------------------------------------------------------------------------------
    //---- Helper methods
    //------------------------------------------------------------------------------
    private void rehash() {
        // hold reference to current table
        HashEntry<K, V>[] oldHashTable = hashtable;

        // replace current table with new table twice current capacity
        hashtable = new HashEntry[2 * hashtable.length];

        // move all the entries from old table to new table
        for (HashEntry<K, V> entry : oldHashTable) {
            if (!entry.isDeleted) {
                insert(entry);
            }
        }
    }

    private HashEntry<K, V> findHashEntry(K key) {
        int index = getHashValue(key);
        int nextIndex = index;

        do {
            HashEntry<K, V> entry = hashtable[nextIndex];
            // case 1: no hash collision during insert
            if (entry == null) {
                return null;
            } else if (key.equals(entry.key)) {
                return entry;
            }

            // case 2: hash collision during insert, continue checking later indexes
            if (nextIndex == hashtable.length - 1) {
                nextIndex = 0;
            } else {
                nextIndex++;
            }
        } while (index != nextIndex);

        return null;
    }

    private InsertResult insert(HashEntry<K, V> entry) {
        int index = getHashValue(entry.key);
        HashEntry<K, V> currentEntry = hashtable[index];

        // no hash collision or duplicate key
        if (currentEntry == null || currentEntry.isDeleted) {
            hashtable[index] = entry;
            return InsertResult.SUCCESS;
        } else if (entry.key.equals(currentEntry.key)) {
            return InsertResult.DUPLICATE;
        }

        // if hash collision, find next empty or deleted index
        int nextIndex = index + 1;
        while (nextIndex != index) {
            // making sure we don't go out of index
            if (nextIndex >= hashtable.length) {
                nextIndex = 0;
            }

            currentEntry = hashtable[nextIndex];
            if (currentEntry == null || currentEntry.isDeleted) {
                hashtable[nextIndex] = entry;
                return InsertResult.SUCCESS;
            } else if (entry.key.equals(currentEntry.key)) {
                return InsertResult.DUPLICATE;
            }
            nextIndex++;
        }

        // if no index found for insertion, then table is full and rehash required
        return InsertResult.REHASH;
    }

    // internal class
    private static class HashEntry<K, V> {
        K key;
        V value;
        boolean isDeleted;

        // constructor
        HashEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // enum for insert result
    private enum InsertResult {
        DUPLICATE, SUCCESS, REHASH
    }
}
