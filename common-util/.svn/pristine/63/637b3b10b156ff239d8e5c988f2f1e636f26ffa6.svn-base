package com.newroad.util.cosure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * @info  : 用于闭包服务的工具类
 * @author: xiangping_yu
 * @data  : 2013-6-9
 * @since : 1.5
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CosureUtils {
	
	private CosureUtils(){
	}

	/**
	 * 合并集合
	 * @param list 合并目标对象
	 * @param merge 合并方式
	 * @return
	 */
	public static <T> T merger(Collection<T> list, Mergeable<T, T> merge){
		if (CollectionUtils.isEmpty(list)) return null;
        T res = null;
        for (T t : list) {
            if (t!=null) {
                if (res==null)res = t;
                else res = merge.merger(res, t);
            }
        }
        return res;
	}
	
	/**
	 * 合并集合 将list1,list2中同一个对象合并
	 * @param list1 合并目标对象1
	 * @param list2 合并目标对象2
	 * @param conver 对象比较转换器
	 * @param merge 合并方式
	 * @return
	 */
	public static <Q, T, M> List<Q> merger(Collection<T> list1,Collection<T> list2,Convertable<T, M> conver, Mergeable<Q, T> merge){
		if (CollectionUtils.isEmpty(list1)) return Collections.emptyList();
        List<Q> res = new ArrayList<Q>(list1.size());
        for (T t : list1) {
        	T t2 = filter(list2, conver.convert(t), conver);
            res.add(merge.merger(t, t2));
        }
        return res;
	}
	
	/**
	 * 统计集合
	 * @param list 统计的目标对象
	 * @param statist 统计方式
	 * @return
	 */
	public static <T,Q> T statistic(Collection<Q> list,Statisticable<T, Q> statist, T def){
		if (CollectionUtils.isEmpty(list)) return def;
        T res = def;
        for (Q q : list) {
            if (q!=null) {
                res = statist.statistic(res, q);
            }
        }
        return res==null ? def : res;
	}
	
	/**
	 * 转换集合
	 * @param list 转换的目标对象
	 * @param conver 转换方式
	 */
	public static <Q1, Q extends Q1, V> List<V> convert(Collection<Q> list, Convertable<Q1, V> conver) {
		return convert(list, Opt.UnNullable_UnRepeatable, new Convertable[] { conver });
	}
	public static <Q1, T1, Q extends Q1, T extends T1, V> List<V> convert(Collection<Q> list, Convertable<Q1, T> conver1, Convertable<T1, V> conver2) {
		return convert(list, Opt.UnNullable_UnRepeatable, new Convertable[] { conver1, conver2 });
	}
	public static <Q1, T1, M1, Q extends Q1, T extends T1, M extends M1, V> List<V> convert(Collection<Q> list, Convertable<Q1, T> conver1, Convertable<T1, M> conver2, Convertable<M1, V> conver3) {
		return convert(list, Opt.UnNullable_UnRepeatable, new Convertable[] { conver1, conver2 , conver3 });
	}
	public static <V> List<V> convert(Collection list, Opt opt, Convertable... converts) {
		if (CollectionUtils.isEmpty(list)) return Collections.emptyList();
		if (opt == null) opt = Opt.UnNullable_UnRepeatable;
		for (Convertable conver : converts) {
			list = execConver(list, opt, conver);
		}
		return (List<V>) list;
	}
	 
	/**
	 * 查找集合中元素
	 * @param list 被查找集合
	 * @param lastV  目标值
	 * @param convert 转换方式
	 * @return 查找结果  返回第一个匹配结果
	 */
	public static <Q1, Q extends Q1, T> Q filter(Collection<Q> list, T lastV, Convertable<Q1, T> conver) {
		return filter(list, lastV, new Convertable[] { conver });
	}
	public static <Q1, M1, Q extends Q1, M extends M1, T> Q filter(Collection<Q> list, T lastV, Convertable<Q1, M> conver, Convertable<M1, T> conver1) {
		return filter(list, lastV, new Convertable[] { conver, conver1 });
	}
	public static <Q1, M1, R1, Q extends Q1, M extends M1, R extends R1, T> Q filter(Collection<Q> list, T lastV, Convertable<Q1, M> conver, Convertable<M1, R> conver1, Convertable<R1, T> conver2) {
		return filter(list, lastV, new Convertable[] { conver, conver1,conver2});
	}
	public static <Q, T> Q filter(Collection<Q> list, T lastV, Convertable... convert) {
		if (CollectionUtils.isEmpty(list)) return null;
        for (Q q : list) {
            Object t = q;
            for (Convertable cv : convert) {
                if (q==null) break;
                t = cv.convert(t);
            }
            if (lastV!=null && lastV.equals(t)) return q;
        }
        return null;
    }
	
	/**
	 * 查找集合中元素
	 * @param list 被查找集合
	 * @param lastV  目标值
	 * @param convert 转换方式
	 * @return 查找结果 （列表）
	 */
	public static <Q1, Q extends Q1, T> List<Q> filterList(Collection<Q> list, T lastV, Convertable<Q1, T> conver) {
		return filterList(list, lastV, new Convertable[] { conver });
	}
	public static <Q1, M1, Q extends Q1, M extends M1, T> List<Q> filterList(Collection<Q> list, T lastV, Convertable<Q1, M> conver, Convertable<M1, T> conver1) {
		return filterList(list, lastV, new Convertable[] { conver, conver1 });
	}
	public static <Q1, M1, R1, Q extends Q1, M extends M1, R extends R1, T> List<Q> filterList(Collection<Q> list, T lastV, Convertable<Q1, M> conver, Convertable<M1, R> conver1, Convertable<R1, T> conver2) {
		return filterList(list, lastV, new Convertable[] { conver, conver1,conver2});
	}
	public static <Q, T> List<Q> filterList(Collection<Q> list, T lastV, Convertable... convert) {
		if (CollectionUtils.isEmpty(list)) return Collections.emptyList();
		List<Q> res = new ArrayList<Q>();
        for (Q q : list) {
            Object t = q;
            for (Convertable cv : convert) {
                if (q==null) break;
                t = cv.convert(t);
            }
            if (lastV!=null && lastV.equals(t)) res.add(q);
        }
        if (CollectionUtils.isEmpty(res)) return Collections.emptyList();
        return res;
    }
	
	/**
	 * 查找集合中元素集合
	 * @param list 被查找集合
	 * @param lastV  目标集合
	 * @param convert 转换方式
	 * @return 查找结果  （集合中查找集合）
	 */
	public static <Q, T> List<Q> filter(Collection<Q> list, List<T> lastV, Convertable ... convert) {
        if (CollectionUtils.isEmpty(list) || CollectionUtils.isEmpty(lastV))  return Collections.emptyList();
        List<Q> res = new ArrayList<Q>(list.size());
        for (Q q : list) {
        	Object t = q;
            for (Convertable con : convert) {
                if (q==null) break;
                t = con.convert(t);
            }
            if (lastV.contains(t)) {
               res.add(q);
            }
        }
        return res;
    }
	
	/**
	 * 对象比对
	 * @param comparable 比对方式
	 * @param t  比对结果
	 * @param v1  比对目标
	 * @param v2  比对列表
	 * @param callback  回调函数
	 */
	public static <T, V, V2, T2> T compare(Comparable<T, V, V2, T2> comparable, T t ,V v1, V2 v2, CallBack<V, T2> callback){
		comparable.compare(t, v1, v2, callback);
		return t;
	}
	
	/**
	 * 取集合最大值
	 */
	public static <T> T max(Collection<T> t, Comparator<T> comparator) {
		return Collections.max(t, comparator);
	}
	public static <T> List<T> maxList(Collection<T> t, Comparator<T> comparator) {
		if(comparator==null) {
			return (List<T>) maxList((Collection<SelfComparable>)t);
		} else {
			List<T> max = new ArrayList<T>(5);
			Iterator<T> i = t.iterator();
			T candidate = i.next();
			max.add(candidate);
			
		    while (i.hasNext()) {
			    T next = i.next();
			    int compare = comparator.compare(next, candidate);
			    if (compare > 0) {
			    	max.clear();
			    	candidate = next;
		        	max.add((T)next);
			    } else if(compare == 0) {
			    	max.add((T)next);
			    }
			}
		    return max;
		}
	}
	private static <T extends Object & java.lang.Comparable<? super T>> List<T> maxList(Collection<T> t) {
		List<T> max = new ArrayList<T>(5);
		
		Iterator<T> i = (Iterator<T>) t.iterator();
		T candidate = i.next();
		max.add(candidate);

	    while (i.hasNext()) {
	        T next = i.next();
	        int compare = next.compareTo(candidate);
	        if (compare > 0) {
	        	max.clear();
	        	candidate = next;
	        	max.add((T)next);
	        } else if(compare == 0) {
	        	max.add((T)next);
	        }
		}
	    return max;
	}
	
	/**
	 * 取集合最小值
	 */
	public static <T> T min(Collection<T> t, Comparator<T> comparator) {
		return Collections.min(t, comparator);
	}
	public static <T> List<T> minList(Collection<T> t, Comparator<T> comparator) {
		if(comparator==null) {
			return (List<T>) minList((Collection<SelfComparable>)t);
		} else {
			List<T> min = new ArrayList<T>(5);
			Iterator<T> i = t.iterator();
			T candidate = i.next();
			min.add(candidate);
			
		    while (i.hasNext()) {
			    T next = i.next();
			    int compare = comparator.compare(next, candidate);
			    if (compare < 0) {
			    	min.clear();
			    	candidate = next;
		        	min.add((T)next);
			    } else if(compare == 0) {
			    	min.add((T)next);
			    }
			}
		    return min;
		}
	}
	private static <T extends Object & java.lang.Comparable<? super T>> List<T> minList(Collection<T> t) {
		List<T> min = new ArrayList<T>(5);
		
		Iterator<T> i = (Iterator<T>) t.iterator();
		T candidate = i.next();
		min.add(candidate);

	    while (i.hasNext()) {
	        T next = i.next();
	        int compare = next.compareTo(candidate);
	        if (compare < 0) {
	        	min.clear();
	        	candidate = next;
	        	min.add((T)next);
	        } else if(compare == 0) {
	        	min.add((T)next);
	        }
		}
	    return min;
	}
	private interface SelfComparable extends java.lang.Comparable<SelfComparable> {}
	
	private static List execConver(Collection list, Opt opt, Convertable closure) {
		List res = new ArrayList(list.size());
		for (Object q : list) {
			opt.execValue(q, closure, res);
		}
		return res;
	}
	
	/**
	 * 合并策略
	 */
	public static enum Opt {
		/**
		 * 允许空  允许重复
		 */
		Nullable_Repeatable {
			@Override
			void execValue(Object q, Convertable conver, List list) {
				Object res = null;
				if (q != null) {
					res = conver.convert(q);
				}
				list.add(res);
			}
		},
		
		/**
		 * 不允许空  允许重复
		 */
		UnNullable_Repeatable {
			@Override
			void execValue(Object q, Convertable conver, List list) {
				if (q == null) return;
				Object res = conver.convert(q);
				if (res == null) return;
				list.add(res);
			}
		},
		
		/**
		 * 允许空 不允许重复
		 */
		Nullable_UnRepeatable {
			@Override
			void execValue(Object q, Convertable conver, List list) {
				Object res = null;
				if (q != null) {
					res = conver.convert(q);
				}
				addAsUnique(list, res);
			}
		},
		
		/**
		 * 不允许空 不允许重复
		 */
		UnNullable_UnRepeatable {
			@Override
			void execValue(Object q, Convertable conver, List list) {
				if (q == null) return;
				Object res = conver.convert(q);
				if (res == null) return;
				addAsUnique(list, res);  
			}
		};

		abstract void execValue(Object q, Convertable conver, List list);
		void addAsUnique(List list, Object res) {
			int indexOf = list.indexOf(res);
			if (indexOf < 0) {
				list.add(res);
			} else {
				Object object = list.get(indexOf);
				if (object instanceof Mergeable) {  // 加入时如果需要合并 则合并后替换原来的值
					list.set(indexOf, ((Mergeable) object).merger(object, res));
				}
			}
		}
	}
}
