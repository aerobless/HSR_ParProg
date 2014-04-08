package exercise03;

import java.util.concurrent.atomic.AtomicReference;

// TODO: Implement lock-free stack
public class LockFreeStack<T> implements Stack<T> {
	AtomicReference<Node<T>> top = new AtomicReference<>();
	
  // TODO: Design stack node class
	private static class Node <E>{
		public final E item;
		public Node<E> next;
		
		public Node(E item) {
			this.item = item;
		}
		
		public void setNext(Node<E> item){
			next = item;
		}
	}
  
  public void push(T value) {
    // TODO: Implement lock-free push operation
	  Node<T> newNode = new Node<>(value);
	  Node<T> current;
	  do{
		  current = top.get();
		  newNode.setNext(current);
	  } while (!top.compareAndSet(current, newNode));
  }
  
  public T pop() {
    // TODO: Implement lock-free push operation
    throw new RuntimeException("not implemented");
  }
  
  
}
