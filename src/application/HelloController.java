package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;


import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    int matrixSize=0;
    double[] result;
    double[][] equationMatrix;
    int row;
    int col;
    int entered=0;
    @FXML
    private Label l1;
    @FXML
    private Label l2;
    @FXML
    private Label l3;
    @FXML
    private Label error;
    @FXML
    private Label editRow;
    @FXML
    private Label editCol;
    @FXML
    private Spinner<Integer> size;
    @FXML
    private TextArea text;
    @FXML
    private TextField value;
    @FXML
    private TextField rowIndex;
    @FXML
    private TextField columnIndex;
    @FXML
    private Button next;
    @FXML
    private Button back;
    @FXML
    private CheckBox edit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
            page1();
    }
    @FXML
    protected void start()
    {
        if(next.getText().equals("Next"))
        {
            entered=0;									//No data entered yet
            matrixSize=size.getValue();					//set matrix size
            result=new double[matrixSize];			//set result matrix
            row=matrixSize;
            col=matrixSize+1;
            equationMatrix= new double[row][col];		//set equation matrix
            
            page2();									//show Page2 of entering equations
            

        }
        else if(edit.isSelected()&&edit.isVisible())		//edit value
        {

            error.setText("");
            int mcol=matrixSize+1;
            int row=0;
            int col=0;
            boolean isNumber =false;
            try {
                 row= Integer.parseInt(rowIndex.getText());				//check entered index is valid
                 col = Integer.parseInt(columnIndex.getText());
                 isNumber=true;
            }catch (Exception e)
            {
                error.setText("Index must be Integer Number Only");
            }
            if(isNumber && ((row-1)*mcol+col)<=entered)
            {
                error.setText("");
                boolean number=false;
                double val=0;
                try {
                    val= Double.parseDouble(value.getText());		//check entered edited value is valid
                    number =true;
                }catch(Exception e)
                {
                    error.setText("Enter Numbers only");
                }
                if(number)
                {
                    equationMatrix[row-1][col-1]=val;
                    text.setText(getText(equationMatrix,entered));
                    value.setText("");
                    endEdit();
                    
                    if(entered==matrixSize*(matrixSize+1))			//if matrix is filled
                    {
                        end();
                    }
                }
                else
                {
                    error.setText("Value Must Be Number");
                }
            }
            else
            {
                error.setText("Enter Currect Index");
            }
        }
       else if(next.getText().equals("Enter"))  //enter value
        {

            error.setText("");
            boolean number=false;
            double val=0;
            try {
                 val= Double.parseDouble(value.getText());	//check entered value is valid
                 number =true;
            }catch(Exception e)
            {
                error.setText("Enter Numbers only");
            }
            if(number)		//if value is valid
            {
                equationMatrix[(entered - entered % col) / col][entered % col]=val;  //equation of get index of number in equation matrix
                entered++;
                if(entered==1)		//make edit value available
                {
                	edit.setVisible(true);
                }
                if(entered==col*row)
                {
                    end();
                }
                else
                {
                	value.requestFocus();
                }
                text.setText(getText(equationMatrix,entered));
                value.setText("");
                
            }
        }
        else if(next.getText().equals("calculate"))
        {
        	
        	
            error.setText("");
            boolean solve=true;
            double[][] matrix2=new double[row][col];
            copyValue(equationMatrix, matrix2);
            if(hasZeroRow(matrix2)) 
            {
                error.setText("There is Zero Row !!!");
                solve=false;
            }
            else if(twoRepeatedEquations(matrix2))
            {
                error.setText("There is Two equation repeated or eq1=const*eq2 !!!");
                solve=false;
            }
            if(solve)
            {
                
	            if( equationMatrix.length>0 && hasZeroRow(matrixElemination(matrix2)) )
	            {
	                error.setText("There is Wrong Equation !!!");
	            }
	            else
	            {
	            	edit.setVisible(false);
	            	getResult();
	            }

            }
        }
        else if(next.getText().equals("Back"))
        {
            page1();
        }
    }
    @FXML
    protected void row(KeyEvent event)
    {
        if(event.getCode()==event.getCode().ENTER)
            columnIndex.requestFocus();
    }

    @FXML
    protected void col(KeyEvent event)
    {
        if(event.getCode()==event.getCode().ENTER)
            value.requestFocus();
    }

    @FXML
    protected void start2(KeyEvent event)
    {
        if(event.getCode()==event.getCode().ENTER)
            start();
    }

    @FXML
    protected void editClick(KeyEvent event)
    {
        if(event.getCode()==event.getCode().CONTROL)
        {
            if(edit.isVisible())
            {
            	value.setVisible(true);
                edit.setSelected(true);
                editRow.setVisible(true);
                editCol.setVisible(true);
                rowIndex.setVisible(true);
                columnIndex.setVisible(true);
                rowIndex.requestFocus();
            }
        }
    }

    @FXML
    protected void start3(KeyEvent event)
    {
        if(event.getCode()==event.getCode().ENTER)
        {
            next.setText("Next");
            start();
        }

    }

    @FXML
    protected void back()
    {
        page1();
    }

    @FXML
    protected void check()
    {
        if(edit.isSelected())
        {
            if(!value.isVisible())
            {
                value.setVisible(true);
                next.setText("Enter");
            }

            editRow.setVisible(true);
            editCol.setVisible(true);
            rowIndex.setVisible(true);
            columnIndex.setVisible(true);

            rowIndex.requestFocus();
        }
        else if(!edit.isSelected())
        {
            if(entered==matrixSize*(matrixSize+1))
            {
                value.setText("");
                value.setVisible(false);
                next.setText("calculate");
            }
            rowIndex.setText("");
            columnIndex.setText("");
            editRow.setVisible(false);
            editCol.setVisible(false);
            rowIndex.setVisible(false);
            columnIndex.setVisible(false);
        }
    }

    
    
    //view Controlling
    public void endEdit()
    {
    	edit.setSelected(false);
    	
    	rowIndex.setText("");
        columnIndex.setText("");
        value.setText("");
        
        editRow.setVisible(false);
        editCol.setVisible(false);
        rowIndex.setVisible(false);
        columnIndex.setVisible(false);
        
        
        value.requestFocus();
    }
    public void end()
    {
    	value.setVisible(false);
        edit.setSelected(false);
        
        value.setText("");
        next.setText("calculate");
        
        next.requestFocus();
        
    }
    

    public void getResult()
    {
    	String matrixText=getText(equationMatrix,entered);
    	equationMatrix=matrixElemination(equationMatrix);
        result(equationMatrix);
        text.setText(printResult(result));
        text.setText(text.getText()+"\n\nYour Matrix --->\n\n"+matrixText+"\n---------------------------------------\nHappy To Help You ^_^\nDeveloped By: Eng/ Mohamed Ezzat Mohamed\nContact: MEzzatIbrahim0@gmail.com");
        next.setText("Back");
    }
    

    
	public void page1()
    {
    	editRow.setVisible(false);
    	
        text.setText("");
        value.setText("");
        text.setText("");
        error.setText("");
        next.setText("Next");
        
        value.setVisible(false);
        text.setVisible(false);
        back.setVisible(false);
        edit.setVisible(false);
        
        editCol.setVisible(false);
        rowIndex.setVisible(false);
        columnIndex.setVisible(false);
        
        size.requestFocus();
    }

    

	public void page2()
    {
    	next.setText("Enter");
        value.setText("");
        text.setText(getText(equationMatrix,entered));	//fill text field with empty matrix
    	
        edit.setSelected(false);
        
    	text.setVisible(true);
        back.setVisible(true);
        value.setVisible(true);
        
        value.requestFocus();

    }
    
    
    
    //calculations


    public String getText(double[][] matrix,int No)
    {
        String a="";
        String head="";
        int max=3;
        int row=matrix.length;
        int col=matrix[0].length;

        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                if(String.valueOf(matrix[i][j]).length()>max)
                {
                    max=String.valueOf(matrix[i][j]).length();
                }
            }
        }

        if(String.valueOf(matrixSize).length()+1>max)
        {
            max=String.valueOf(matrixSize).length()+1;
        }

        for(int i=0;i<col;i++)
        {
            String co="";
            if(i==(matrix[0].length-1))
            {
                String c="";
                for(int k=0;k<(max-3);k++)
                {
                    c+=" ";
                }
                head+="|  Ans"+co+"  ";
            }
            else
            {
                for(int j=0;j<(max-String.valueOf(i+1).length()-1);j++)
                {
                    co+=" ";
                }
                head+="V"+(i+1)+co+"  ";
            }

        }
        String line="";
        for(int i=0;i<head.length();i++)
        {
            line+="-";
        }
        head+="\n"+line;
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                String co="";
                if(j==col-1)
                {
                    a+="|  ";
                }
                if((i*col)+(j+1)<=No)
                {
                    for(int k=0;k<(max-String.valueOf(matrix[i][j]).length());k++)
                    {
                        co+=" ";
                    }
                    a+=matrix[i][j]+co+"  ";
                }
                else if((i*col)+(j+1)==No+1)
                {
                    for(int k=0;k<(max-2);k++)
                    {
                        co+=" ";
                    }
                    a+="░░"+co+"  ";//
                }
                else
                {
                    for(int k=0;k<(max-2);k++)
                    {
                       co+=" ";
                    }
                    a+="██"+co+"  ";//
                }

            }
            a+="\n";
        }
        a=head+"\n"+a;
        return a;
    }

    

    private  double[][] matrixElemination(double[][] m) {
		for(int i=0;i<m.length-1;i++)
		{
				if(m[i][i]==0)
				{
					boolean zero=true;
					for(int l=i+1;l<m.length;l++)
					{
						if(m[l][i]!=0)
						{
							 double[] replace=new double[m[0].length];
		                        for(int l2=0;l2<m[0].length;l2++)
		                        {
		                            replace[l2]=m[l][l2];
		                            m[l][l2]=m[i][l2];
		                            m[i][l2]=replace[l2];
		                        }
		                        
		                        zero=false;
						}
						if(!zero)
						{
							
							for(int j=i+1;j<m.length;j++)
							{
								double c=m[j][i]/m[i][i];
								for(int k=0;k<m[0].length;k++)
								{
									m[j][k]=c*m[i][k]-m[j][k];
								}
							}
						}
					}
				}
				else
				{
					for(int j=i+1;j<m.length;j++)
					{
						double c=m[j][i]/m[i][i];
						for(int k=0;k<m[0].length;k++)
						{
							m[j][k]=c*m[i][k]-m[j][k];
						}
					}
				}
		}
		return m;
	}

    
    public  String printResult(double[] result)
    {
    	
        String resultText="";
        String part="";
        resultText+="Results--->\n";
        for(int i=0;i<result.length;i++)
        {
        	part=String.valueOf(result[i]);
        	if(part.contains("."))
        	{
        		if(part.substring(part.indexOf(".")).length()>=8)
        		{
        			part=part.substring(0,part.indexOf(".")+8);
        		}
        	}
            resultText+="v"+(i+1)+" = "+part+"\n";
        }
        return resultText;
    }

       

    
    public  boolean hasZeroRow(double[][] matrix)
	{
		for(int i=0;i<matrix.length;i++)
		{
			boolean zero=true;
			for(int j=0;j<matrix.length;j++)
			{
				if(matrix[i][j]!=0)
				{
					zero=false;
				}
			}
			if(zero)
			{
				return true;
			}
		}
		return false;
	}
  
    
    public  boolean twoRepeatedEquations(double[][] matrix)
    {
    	for(int i=0;i<matrix.length-1;i++)
		{
			for(int j=i+1;j<matrix.length;j++)
			{
				if(isRepeated(matrix[i],matrix[j]))
				{
					return true;
				}
			}
		}
		return false;
    }

    
    public boolean isRepeated(double[] a,double[] b)
	{
    	double k=0;
    	boolean save=false;
    	for(int i=0;i<a.length-1;i++)
    	{
    		if(a[i]!=0&&b[i]!=0)
    		{
    			k=a[i]/b[i];
    			save=true;
    		}
    	}
		if(!save)
		{
			return false;
		}
		for(int i=0;i<a.length-1;i++)
		{
			if(a[i]!=k*b[i])
			{
				return false;
			}
		}
		return true;
		
	}
    
    
    public  void result(double[][] matrix)
    {
    	int row=matrix.length;
		for(int i=row-1;i>=0;i--)
		{
			double x=0;
			for(int k=row-1;k>i;k--)
			{
				x+=result[k]*matrix[i][k];
			}
			result[i]=(matrix[i][row]-x)/matrix[i][i];
			if(String.valueOf(result[i]).equals("-0.0"))
			{
				result[i]=0.0;
			}
					
		}
    }

    
    public  void copyValue(double[][] a,double[][] b)
    {
        for(int i=0;i<a.length;i++)
        {
            for(int j=0;j<a[0].length;j++)
            {
                b[i][j]=a[i][j];
            }
        }
    }


    public void print(double[][] a)
    {
    	for(int i=0;i<a.length;i++)
    	{
    		for(int j=0;j<a[0].length;j++)
    		{
    			System.out.print(a[i][j]+"  ");
    		}
    		System.out.print("\n");
    	}
    	
    }
}