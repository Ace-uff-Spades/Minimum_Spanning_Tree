package apps;

import structures.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import apps.PartialTree.Arc;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		
		PartialTreeList L = new PartialTreeList();
		
		if(graph.vertices.length == 0){
			return L; 
		}
		else{
			Vertex[] vert = graph.vertices; 
			for(int i =0; i<vert.length; i++){
				L.append(new PartialTree(vert[i]));
			}
			Iterator<PartialTree> itr = L.iterator();
			while(itr.hasNext()){
				PartialTree update = itr.next();
				Vertex one = update.getRoot();
				MinHeap<Arc> arc = update.getArcs();
				if(one.neighbors != null){
					Vertex.Neighbor bor = one.neighbors; 
					while(bor!=null){
						PartialTree.Arc a = new PartialTree.Arc(one,bor.vertex, bor.weight);
						arc.insert(a);
						if(bor.next!=null)
							bor = bor.next;
						else
							bor = null;
					}		
				}				
			}
			return L;
		}
		
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
				
		ArrayList<PartialTree.Arc> Mst = new ArrayList<PartialTree.Arc>();
		Iterator<PartialTree> itr = ptlist.iterator(); 

		while(itr.hasNext()){
			
			itr.next();
			if(itr.hasNext()==false){
				break;
			}
			
			//Taking the current tree
			PartialTree current = ptlist.remove();

			
			//taking the min that doesn't repeat the tree's root
			PartialTree.Arc min = current.getArcs().deleteMin();
			while(current.getRoot().name.contains(min.v2.name) && !current.getArcs().isEmpty()){
				min = current.getArcs().deleteMin();
			}

			//Adding to the minimum spanning tree
			Mst.add(min);
			
			//finding the next tree that contains v2 
			PartialTree next = ptlist.removeTreeContaining(min.v2);
			MinHeap<Arc> second = next.getArcs();
			
			
			//merge arcs and append the resultant PartialTree to the PartialTreeList
			current.getArcs().merge(second);
			next.getRoot().parent = current.getRoot();
			ptlist.append(current);
		    
		}

		return Mst;
	}
	public static void display(PartialTreeList Tlist){
		Iterator<PartialTree> itr = Tlist.iterator();
		while(itr.hasNext()){
			System.out.println(itr.next());
		}
	}
	public static void main(String[] args) throws IOException{
		Graph g = new Graph("graph1.txt");
	//	g.print();
		MST one = new MST();
		ArrayList<PartialTree.Arc> path = one.execute(one.initialize(g));
		for(int i = 0; i<path.size(); i++){
			System.out.println(path.get(i));
		}
	}
}

