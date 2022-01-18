package share;


public class convert {
	public String btoe(String a)
	{
		//System.out.print(a);
		int b=0;
		String sum ="";
		while(b<a.length())
		{
			if(a.charAt(b)=='১')
			{
				sum=sum+'1';
			}
			else if(a.charAt(b)=='২')
			{
				sum=sum+'2';
			}
			else if(a.charAt(b)=='৩')
			{
				sum=sum+'3';
			}
			else if(a.charAt(b)=='৪')
			{
				sum=sum+'4';
			}
			else if(a.charAt(b)=='৫')
			{
				sum=sum+'5';
			}
			else if(a.charAt(b)=='৬')
			{
				sum=sum+'6';
			}
			else if(a.charAt(b)=='৭')
			{
				sum=sum+'7';
			}
			else if(a.charAt(b)=='৮')
			{
				sum=sum+'8';
			}
			else if(a.charAt(b)=='৯')
			{
				sum=sum+'9';
			}
			else if(a.charAt(b)=='০')
			{
				sum=sum+'0';
			}
			else if(a.charAt(b)=='.'){
				sum=sum+'.';
			}
			else{
				sum=sum+a.charAt(b);
			}
			b++;
		}
		return sum;
		
	}
	public String etob(String a)
	{
		int b=0;
		String sum="";
		while(b<a.length())
		{
			
			if(a.charAt(b)=='1')
			{
				sum=sum+'১';
			}
			else if(a.charAt(b)=='2')
			{
				sum=sum+'২';
			}
			else if(a.charAt(b)=='3')
			{
				sum=sum+'৩';
			}
			else if(a.charAt(b)=='4')
			{
				sum=sum+'৪';
			}
			else if(a.charAt(b)=='5')
			{
				sum=sum+'৫';
			}
			else if(a.charAt(b)=='6')
			{
				sum=sum+'৬';
			}
			else if(a.charAt(b)=='7')
			{
				sum=sum+'৭';
			}
			else if(a.charAt(b)=='8')
			{
				sum=sum+'৮';
			}
			else if(a.charAt(b)=='9')
			{
				sum=sum+'৯';
			}
			else if(a.charAt(b)=='0')
			{
				sum=sum+'০';
			}
			else if(a.charAt(b)=='.')
			{
				sum=sum+'.';
			}
			else{
				sum=sum+a.charAt(b);
			}
			
			b++;
		}
		//System.out.print(sum);
		return sum;
	}
}
