class Node:
    def __init__(self, data):
        self.next = None
        self.prev = None
        self.data = data


class DLL:
    def __init__(self):
        self.head = None
        self.tail = None
        self.size = 0;

    def display(self):
        if self.head is None:
            print("empty linked list")
        else:
            temp = self.head
            while temp:
                print(temp.data, end=" ")
                temp = temp.next
            print()

    def addLast(self, val):
        self.size = self.size + 1;
        if self.head is None:
            self.head = Node(val)
            self.tail = self.head
        else:
            temp = Node(val)
            self.tail.next = temp
            temp.prev = self.tail
            self.tail = temp;

    def addFirst(self, val):
        self.size = self.size+1;
        if self.head is None:
            self.head = Node(val)
            self.tail = self.head
        else:
            temp = Node(val)
            self.head.prev = temp
            temp.next = self.head
            self.head = temp;

    def contains(self, val):
        if self.head is None:
            return False
        else:
            temp = self.head
            while temp:
                if temp.data == val:
                    return True
                temp = temp.next
        return False

    def insert(self, ind, val, ):
        if 0 > ind >= self.size:
            print("plz enter valid index")
        elif ind == 0:
            self.addFirst(val)
        elif ind == self.size:
            self.addLast(val)
        else:
            self.size = self.size + 1
            tempInd = 0
            tempNode = self.head
            prev = None
            while tempInd != ind:
                tempInd = tempInd+1
                prev = tempNode
                tempNode = tempNode.next
            newNode = Node(val)
            newNode.prev = prev
            newNode.next = prev.next
            prev.next.prev = newNode
            prev.next = newNode

    def removeFirst(self):
        if self.head is None:
            print("can't remove because its empty")
        elif self.head == self.tail:
            self.size = self.size - 1
            self.head = None
            self.tail = None
        else:
            self.size = self.size - 1
            self.head = self.head.next
            self.head.prev = None

    def removeLast(self):
        if self.head is None:
            print("can't remove because its empty")
        elif self.head == self.tail:
            self.size = self.size - 1
            self.head = None
            self.tail = None
        else:
            self.size = self.size - 1
            self.tail = self.tail.prev
            self.tail.next = None

    def remove(self, ind ):
        if 0 > ind >= self.size:
            print("plz enter valid index")
        elif ind == 0:
            self.removeFirst()
        elif ind == self.size-1:
            self.removeLast()
        else:
            self.size = self.size - 1
            tempInd = 0
            tempNode = self.head
            prev = None
            while tempInd != ind:
                tempInd = tempInd+1
                prev = tempNode
                tempNode = tempNode.next
            prev.next.next.prev = prev
            prev.next = prev.next.next

    def indexOf(self,val):
        if self.contains(val):
            temp = self.head
            ind = 0
            while temp.data != val:
                ind = ind + 1
                temp = temp.next
            return ind
        else:
            return -1


class LinkedHashSet(DLL):
    def __init__(self):
        super().__init__()

    def add(self , val):
        # print(self.contains(val))
        if self.contains(val):
            return False
        self.addLast(val)
        return True


class Surya:
    st = LinkedHashSet()
    st.add(4)
    print(st.add(4))
    st.display()
 
