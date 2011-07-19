package com.jetdrone.map.index;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.jetdrone.map.BoundingBox;

@SuppressWarnings("serial")
public class QTree<T extends BoundingBox> implements Serializable {
	
	private final BoundingBox bb;
	private final int treeDepth;
	private final QTree<T> nw;
	private final QTree<T> ne;
	private final QTree<T> sw;
	private final QTree<T> se;
	private List<T> elements;

	public QTree(BoundingBox bbox) {
		this(6, bbox);
	}

	public QTree(int maxTreeDepth, BoundingBox bbox) {
		bb = bbox;
		treeDepth = maxTreeDepth;

		if (treeDepth > 1) {
			nw = new QTree<T>(treeDepth - 1, bb.getNWQuadrant());
			ne = new QTree<T>(treeDepth - 1, bb.getNEQuadrant());
			sw = new QTree<T>(treeDepth - 1, bb.getSWQuadrant());
			se = new QTree<T>(treeDepth - 1, bb.getSEQuadrant());
		} else {
			nw = null;
			ne = null;
			sw = null;
			se = null;
		}
	}

	public void add(T t) {
		if (treeDepth == 1) {
			if (t.intersects(bb)) {
				if(elements == null) {
					elements = new LinkedList<T>();
				}
				elements.add(t);
			} else {
				throw new UnsupportedOperationException("Element outside BoundingBox: " + t);
			}
			return;
		}

		boolean bNW = t.intersects(nw.getBoundingBox());
		boolean bNE = t.intersects(ne.getBoundingBox());
		boolean bSW = t.intersects(sw.getBoundingBox());
		boolean bSE = t.intersects(se.getBoundingBox());

		int nCount = 0;

		if (bNW == true) {
			nCount++;
		}
		if (bNE == true) {
			nCount++;
		}
		if (bSW == true) {
			nCount++;
		}
		if (bSE == true) {
			nCount++;
		}

		if (nCount > 1) {
			if(elements == null) {
				elements = new LinkedList<T>();
			}
			elements.add(t);
			return;
		}
		if (nCount == 0) {
			throw new UnsupportedOperationException("Element outside BoundingBox: " + t);
		}

		if (bNW == true) {
			nw.add(t);
		}
		if (bNE == true) {
			ne.add(t);
		}
		if (bSW == true) {
			sw.add(t);
		}
		if (bSE == true) {
			se.add(t);
		}
	}

	public boolean remove(T t) {
		if (elements != null && elements.remove(t)) {
			return true;
		}

		if (treeDepth > 1) {
			if (nw.remove(t)) {
				return true;
			}

			if (ne.remove(t)) {
				return true;
			}

			if (sw.remove(t)) {
				return true;
			}

			if (se.remove(t)) {
				return true;
			}
		}

		return false;
	}

	public void clear() {
		if(elements != null) elements.clear();
		
		if (treeDepth > 1) {
			nw.clear();
			ne.clear();
			sw.clear();
			se.clear();
		}
	}

	public int getMaxTreeDepth() {
		return treeDepth;
	}

	public List<T> getAll() {
		List<T> l = new LinkedList<T>();
		if(elements != null) {
			l.addAll(elements);
		}

		if (treeDepth > 1) {
			l.addAll(nw.getAll());
			l.addAll(ne.getAll());
			l.addAll(sw.getAll());
			l.addAll(se.getAll());
		}

		return l;
	}

	public List<T> get(BoundingBox bbox) {
		List<T> v = new LinkedList<T>();

		if (bb.intersects(bbox)) {
			if(elements != null) {
				for (T t : elements) {
					if (t.intersects(bbox)) {
						v.add(t);
					}
				}
			}
			if (treeDepth > 1) {
				v.addAll(nw.get(bbox));
				v.addAll(ne.get(bbox));
				v.addAll(sw.get(bbox));
				v.addAll(se.get(bbox));
			}
		}

		return v;
	}

	public BoundingBox getBoundingBox() {
		return bb;
	}
}
