/*
 * 从某数组删除重复的元素
 * 
 * 
 */
package General_Function;
import java.util.*;
public class Remove_Duplicate 
{
	public static void remove_Duplicate(ArrayList<String> arr)
	{
		for(int i=0;i<arr.size();i++)
		{
			String str=arr.get(i);
			for(int k=i+1;k<arr.size();k++)
				if(str.equals(arr.get(k)))
				{
					arr.remove(k);
					k--;
				}
		}
	}
	public static void main(String[] args)
	{
		ArrayList<String> arr=new ArrayList<String>();
		arr.add("aa");
		arr.add("bb");
		arr.add("cc");
		arr.add("aa");
		arr.add("dd");
		arr.add("aa");
		
		for(int i=0;i<arr.size();i++) System.out.print(arr.get(i)+"  ");
		remove_Duplicate(arr);
		 System.out.println("");
		for(int i=0;i<arr.size();i++) System.out.print(arr.get(i)+"  ");
	}

}
