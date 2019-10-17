package pdtpkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Product {

	public static void main(String[] args) throws ClassNotFoundException,SQLException, NumberFormatException, IOException{
		
		Class.forName("com.mysql.jdbc.Driver");  
		
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/product","root","");  
		
		if (con != null)
		{
			System.out.println("Connected");
		}
		else
		{
			System.out.println("Not Connected");
		}
		
		Statement st=con.createStatement();
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		int choice,ch;
		String u,p;
		do
		{
			System.out.println("\n1. Admin Login");
			System.out.println("2. Agent Login");
			System.out.println("3. Exit");
			System.out.println("Enter your Choice : ");
			choice=Integer.parseInt(br.readLine());
			switch(choice)
			{
				case 1:
						System.out.println("\nADMIN HOME");
						System.out.println("-------------\n");
						System.out.print("Username	:	");
						u=br.readLine();
						System.out.print("Password	:	");
						p=br.readLine();
						int count=0;
						try
						{
							ResultSet rs=st.executeQuery("select user,pwd from admin");
							while(rs.next())
							{
								if(rs.getString(1).equals(u)&&rs.getString(2).equals(p))
								{
								count=1;
								System.out.println("Logged Successfully!!!\n");	
								do
								{
									System.out.println("\n1. Add Product");
									System.out.println("2. Display Inventory Details");
									System.out.println("3. Logout");
									System.out.println("Enter your Choice : ");
									ch=Integer.parseInt(br.readLine());
									switch (ch)
									{
										case 1:
											System.out.println("\nProduct ID : ");
											int pid=Integer.parseInt(br.readLine());
											System.out.println("Product Name : ");
											String name=br.readLine();
											System.out.println("Quantity Available: ");
											int qa=Integer.parseInt(br.readLine());
											System.out.println("Minimum Sell Quantity : ");
											int q=Integer.parseInt(br.readLine());
											System.out.println("Price : ");
											int pr=Integer.parseInt(br.readLine());
											int t=qa*pr;
											try
											{
												st.executeUpdate("insert into items values("+pid+",'"+name+"',"+qa+","+q+","+pr+","+t+")");
												
												                 // INSERT INTO `admin`(`pdt_id`, `name`, `min_sell_quantity`, `price`) VALUES
												System.out.println("\nAdded Product Successfully!!");
											}
											catch(SQLException e1)
											{
												System.out.println("Error in insertion");
											}
											break;
											
										case 2:
											try 
											{
												ResultSet rs1=st.executeQuery("select pdt_id,name,quantity,price,total from items");
												System.out.println("-----------------------------------------------------------------");
												System.out.print("Product ID\tName\t\tQuantity\tPrice\tTotal\n");
												System.out.println("-----------------------------------------------------------------");

												while(rs1.next())
												{
													System.out.print(rs1.getString(1)+"\t\t"+rs1.getString(2)+"\t\t"+rs1.getString(3)+"\t\t"+rs1.getString(4)+"\t"+rs1.getString(5)+"\n");
													
												}
											
											}
											catch(SQLException e1)
											{
												System.out.println("Error");
											}
											break;
											
										case 3:
											break;
											

										default:
											System.out.println("Invalid Input");
									}
								}while(ch!=3);
							}
							
						}
						if(count==0)
								System.out.println("Enter Correct Username and Password!!");

					}
					catch(SQLException e)
					{
							System.out.println("");
					}
					break;
					
				case 2:
					System.out.println("AGENT HOME");
					System.out.println("---------------\n");
					System.out.print("Username	:	");
					u=br.readLine();
					System.out.print("Password	:	");
					p=br.readLine();
					try
					{
						ResultSet rs=st.executeQuery("select id,user,pwd from agent");
						count=0;
						while(rs.next())
						{
							if(rs.getString(2).equals(u)&&rs.getString(3).equals(p))
							{
								count=1;
								int aid=rs.getInt(1);
								//System.out.println(aid);//13
								System.out.println("Logged Successfully!!");
								do
								{
								System.out.println("\n1. Buy / Sell");
								System.out.println("2. Show History");
								System.out.println("3. Logout");
								System.out.println("Enter your Choice : ");
								ch=Integer.parseInt(br.readLine());
								switch (ch)
								{
									case 1:
										System.out.print("Enter Product ID : ");
										int id=Integer.parseInt(br.readLine());
										rs=st.executeQuery("select pdt_id,name,price,min_quantity,quantity from items"); 
										while(rs.next())
										{
											count=0;
											if(rs.getInt(1)==id)
											{
												count=1;
												break;
											}
										}
										if(count==0)
										{
											System.out.println("\nPlease Enter Correct Product ID!!");
											System.out.println("These are the Available Products.....");
											rs=st.executeQuery("select pdt_id,name from items"); 
											System.out.print("\nProduct ID\tName\n");
											System.out.println("---------------------------");
											while(rs.next())
											{
												System.out.print(rs.getString(1)+"\t\t"+rs.getString(2)+"\n");
											}

										}
										else
										{
										rs=st.executeQuery("select pdt_id,name,price,min_quantity,quantity from items"); 
										System.out.print("\nName\t\tPrice\tMin_Quantity\n");
										System.out.println("--------------------------------------------");
										int price=0,qa=0,min=0;
										String n=null;
										while(rs.next())
										{
												if(rs.getInt(1)==id)
												{	
													System.out.print(rs.getString(2)+"\t\t"+rs.getString(3)+"\t\t"+rs.getString(4)+"\n");
													id=rs.getInt(1);
													n=rs.getString(2);
													price=rs.getInt(3);
													min=rs.getInt(4);
													qa=rs.getInt(5);
													break;
												}
										}				
										System.out.println("\nEnter the Quantity : ");
										int qneed=Integer.parseInt(br.readLine());
										System.out.print("You want to Buy or Sell (B/S) ? : ");
										String option=br.readLine();
										//System.out.println(min);
										//System.out.println(qa);
										if(option.equalsIgnoreCase("buy"))
										{								
											if(min<=qneed && qa>=qneed)
											{
												int qbal=qa-qneed;		
												int ucost=price*qneed;
												System.out.println("Total Cost : "+ucost);
												System.out.println("Confirm Booking ? : ");
												String resp=br.readLine();
												if(resp.equalsIgnoreCase("yes"))
												{
													System.out.println("Thank You For The Transaction!!!");
													st.executeUpdate("update items set quantity = "+qbal+" where pdt_id = "+id);
													int tcost=qbal*price;
													st.executeUpdate("update items set total = "+tcost+" where pdt_id = "+id);
													st.executeUpdate("insert into trans (ag_id, pdt_id, name, type, quantity, price, total) values ("+aid+","+id+",'"+n+"','buy',"+qneed+","+price+","+ucost+")");
												}
												
											}
											else
											{
												System.out.println("Product is not Available...");
											}
										}
										if(option.equalsIgnoreCase("sell"))
										{
											int addq=qa+qneed;
											int ucost=price*qneed;	
											System.out.println("Total Cost : "+ucost);
											System.out.println("Thank You For The Transaction!!!");
											st.executeUpdate("update items set quantity = "+addq+" where pdt_id = "+id);
											int tcost=addq*price;
											st.executeUpdate("update items set total = "+tcost+" where pdt_id = "+id);
											st.executeUpdate("insert into trans (ag_id, pdt_id, name, type, quantity, price, total) values("+aid+","+id+",'"+n+"','sell',"+qneed+","+price+","+ucost+")");

										}
										}
										break;
										
									case 2:
										
										try
										{
											rs=st.executeQuery("select ag_id, pdt_id, name, type, quantity, price, total from trans order by trans_id");
											System.out.print("Product ID\tName\t\tTransaction\tQuantity\tPrice\tTotal\n");
											System.out.println("-----------------------------------------------------------------------------");
											while(rs.next())
											{
												if(rs.getInt(1)==aid)
													System.out.print(rs.getString(2)+"\t\t"+rs.getString(3)+"\t\t"+rs.getString(4)+"\t\t"+rs.getString(5)+"\t\t"+rs.getString(6)+"\t"+rs.getString(7)+"\n");
											}
										}
										catch(SQLException e1)
										{
											
										}
										break;
									case 3:
										break;
									default:
										System.out.println("Invalid Input");
								}
								}while(ch!=3);
							}
							
						}
						if(count==0)
							System.out.println("Enter Correct Username & Password");
					}
					catch(SQLException e1)
					{
						System.out.println(" ");
					}
					break;
				case 3:
					System.out.println("...Thank You...");
					System.exit(0);
					
				default:
					System.out.println("Invalid Input");
			}					
		}while(true);
	}

}
