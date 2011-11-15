/*
 * 合并数组aa和bb，结果存于aa
 * 
 * 
 */

package General_Function;
import java.util.*;
public class Combine_Two_Arr 
{
	public static void combine_Two_Arr(ArrayList<String> arr_a, ArrayList<String> arr_b)
	{
		for(int i=0;i<arr_b.size();i++)
			arr_a.add(arr_b.get(i));
	}
	
	public static void main(String[] args)
	{
		ArrayList<String> aa=new ArrayList<String>();
		aa.add("a");aa.add("b");
		ArrayList<String> bb=new ArrayList<String>();
		bb.add("c");bb.add("d");
		combine_Two_Arr(aa,bb);
		for(int i=0;i<aa.size();i++)
			System.out.println(aa.get(i));
		for(int i=0;i<bb.size();i++)
			System.out.println(bb.get(i));
	}

}
