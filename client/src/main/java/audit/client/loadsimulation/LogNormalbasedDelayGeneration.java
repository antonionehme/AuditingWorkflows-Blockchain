package audit.client.loadsimulation;
import java.util.Random;

import audit.client.loadsimulation.LogNormalDistribution;
import audit.client.loadsimulation.Math;

import java.io.FileWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//import java.lang.Math;

public class LogNormalbasedDelayGeneration {
   /* public static double mu;
    public static double sigma;*/
    
    public static double LogNormalGenerated(double mu, double sigma) { //Long L=(long) -200;
    	Random r = new Random();
    	double random=r.nextGaussian();
	double logdelay=mu+ (random*sigma);
	double Ret=Math.exp(logdelay);
	return Ret;
    }
    
   /* public static double LogNormalGenerated_z_Gaussian(double mu, double sigma) { //Long L=(long) -200;
    	Random r = new Random();
    	double random=r.nextDouble();
    	double random_2=2*random;
    	
    	
    	Random r2 = new Random();
    	double random_z=r2.nextDouble();//To determine if z is + or -
    	
    	double generated=0;
    	if (random_z>=0) {
    		generated=mu+ (random_2*sigma);
    	} else {generated=mu- (random_2*sigma);}
    	
    	
	double Ret=Math.exp(generated);
	return Ret;
    }*/
    
    
    public static void simulate_delay_csv(double [] experiments_data, int number_of_generated_points, double constant, double tweaked_mu, double tweaked_sigma, String output) throws IOException {
    	
    	String file = output;
    	FileWriter file_writer = new FileWriter(file);
    	String FILE_HEADER = "Constrant "+constant+ " Mu "+ tweaked_mu+ " sigma "+ tweaked_sigma+", ResponseTime with Delay of C*e^(mu+sigma) of the simulation"+"\n";
    	file_writer.append(FILE_HEADER.toString());
    	
    	file_writer.flush(); file_writer.flush();//this is to create the emtpy file
    	
    			LogNormalDistribution lnd= new LogNormalDistribution(experiments_data);
    			
    			FileWriter fileWriter = new FileWriter(file,true);//write on top of the existent file
    			
    	for(int i=0;i<number_of_generated_points;i++) {
    		double runtime_with_delay=LogNormalGenerated(lnd.getMu(),lnd.getSigma())+constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
    		fileWriter.append("iteration "+i+", "+runtime_with_delay+"\n");
    		
    	}
    	fileWriter.flush();
        fileWriter.close();
    }
    
    public static void simulate_delay_Considering_z_negative_csv(double [] experiments_data, int number_of_generated_points, double constant, double tweaked_mu, double tweaked_sigma, String output) throws IOException {
    	//This is wrong. Minus should be at the level of the log, and the delay should always be positive.
    	String file = output;
    	FileWriter file_writer = new FileWriter(file);
    	String FILE_HEADER = "Constrant "+constant+ " Mu "+ tweaked_mu+ " sigma "+ tweaked_sigma+", ResponseTime with Delay of C*e^(mu+Z*sigma) of the simulation"+"\n";
    	file_writer.append(FILE_HEADER.toString());
    	
    	file_writer.flush(); file_writer.flush();//this is to create the emtpy file
    	
    			LogNormalDistribution lnd= new LogNormalDistribution(experiments_data);
    			
    			FileWriter fileWriter = new FileWriter(file,true);//write on top of the existent file
    			
    	for(int i=0;i<number_of_generated_points;i++) {
        //	Random r = new Random();
        //	double random=r.nextDouble();
        	double response_no_delay=LogNormalGenerated(lnd.getMu(),lnd.getSigma());
        	double delay=constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
        	double delay2=constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
        	double delay3=constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
        	double delay4=constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
        	double response_with_delay;
        	
    
        	if(response_no_delay+delay+delay2+delay3+delay4>0) {
    		response_with_delay=response_no_delay+delay+delay2+delay3+delay4;
        	} else response_with_delay=response_no_delay;
    		
    		fileWriter.append("iteration "+i+", "+ response_no_delay+ ", "+response_with_delay+"\n");
    		
    	}
    	fileWriter.flush();
        fileWriter.close();
    }
    
    public static void simulate_delay_Z_data(int start, double [] experiments_data, int number_of_generated_points, double constant, double tweaked_mu, double tweaked_sigma, String output) throws IOException {
    	//Reading Data
    	String file = output;
    	FileWriter file_writer = new FileWriter(file);
    	String FILE_HEADER = "Constrant "+constant+ " Mu "+ tweaked_mu+ " sigma "+ tweaked_sigma+", ResponseTime with Delay of C*e^(mu+Z*sigma) of the simulation"+"\n";
    	file_writer.append(FILE_HEADER.toString());
    	
    	file_writer.flush(); file_writer.flush();//this is to create the emtpy file
    	
    			LogNormalDistribution lnd= new LogNormalDistribution(experiments_data);
    			
    			FileWriter fileWriter = new FileWriter(file,true);//write on top of the existent file
    			
    	for(int i=0;i<number_of_generated_points;i++) {
        //	Random r = new Random();
        //	double random=r.nextDouble();
        	double response_no_delay=experiments_data[start];
        	double delay=constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
        	double delay2=constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
        	double delay3=constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
        	double delay4=constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
        	double response_with_delay;
        	start++;
        	
    
        	if(response_no_delay+delay+delay2+delay3+delay4>0) {
    		response_with_delay=response_no_delay+delay+delay2+delay3+delay4;
        	} else response_with_delay=response_no_delay;
    		
    		fileWriter.append("iteration "+i+", "+ response_no_delay+ ", "+response_with_delay+"\n");
    		
    	}
    	fileWriter.flush();
        fileWriter.close();
    }
    
public static double simulate_delay_time(double constant, double tweaked_mu, double tweaked_sigma) throws IOException {
   
    		double delay=constant*LogNormalGenerated(tweaked_mu,tweaked_sigma);
    		
    		return delay;

}
    
public static void main(String[]args) throws IOException {
	/*Random r = new Random();
	double random=r.nextGaussian();
	System.out.println("Z "+ random);*/
	double [] experiments_data_single = new double [] {618, 624, 659, 657, 19, 28, 50, 78, 14, 24, 37, 45, 14, 20, 27, 47, 14, 20, 34, 49, 12, 17, 30, 53, 11, 20, 29, 38, 14, 19, 29, 37, 14, 17, 29, 33, 12, 22, 32, 39, 11, 19, 29, 36, 12, 17, 31, 35, 13, 17, 25, 41, 11, 18, 27, 35, 11, 16, 27, 37, 10, 17, 28, 37, 10, 16, 30, 34, 11, 21, 29, 39, 11, 21, 28, 38, 13, 18, 30, 34, 13, 17, 30, 34, 11, 16, 34, 48, 8, 13, 24, 38, 9, 14, 23, 38, 10, 15, 26, 33, 9, 14, 25, 31, 9, 14, 22, 35, 10, 17, 20, 33, 9, 13, 28, 32, 8, 13, 23, 34, 10, 12, 22, 36, 9, 15, 24, 31, 10, 13, 29, 32, 9, 13, 21, 33, 8, 13, 23, 30, 7, 13, 20, 29, 8, 12, 22, 31, 9, 13, 20, 31, 8, 20, 23, 41, 9, 14, 20, 30, 8, 13, 24, 32, 7, 12, 23, 29, 9, 13, 20, 35, 8, 12, 21, 30, 8, 12, 20, 30, 8, 14, 26, 30, 7, 14, 21, 30, 9, 13, 22, 29, 9, 12, 25, 32, 9, 13, 21, 30, 545, 498, 529, 567, 17, 21, 38, 64, 11, 17, 26, 43, 11, 16, 25, 41, 11, 17, 30, 33, 10, 18, 28, 43, 14, 15, 27, 39, 10, 18, 28, 39, 10, 15, 26, 33, 10, 14, 23, 37, 10, 14, 24, 39, 10, 14, 22, 34, 14, 14, 27, 35, 9, 15, 25, 37, 9, 14, 22, 33, 10, 14, 24, 34, 10, 14, 22, 35, 10, 15, 25, 34, 11, 14, 23, 42, 8, 16, 25, 34, 9, 15, 24, 32, 10, 14, 28, 36, 8, 14, 22, 63, 9, 13, 24, 43, 9, 13, 22, 32, 9, 13, 21, 35, 8, 12, 20, 30, 10, 13, 22, 30, 9, 19, 26, 31, 8, 14, 21, 33, 9, 14, 20, 33, 564, 606, 612, 759, 17, 23, 46, 61, 12, 16, 31, 49, 19, 17, 29, 40, 13, 18, 29, 40, 10, 16, 25, 43, 14, 16, 28, 38, 10, 14, 24, 34, 11, 14, 27, 33, 11, 20, 26, 39, 10, 13, 23, 41, 10, 16, 26, 42, 10, 15, 25, 38, 9, 18, 31, 35, 9, 18, 22, 44, 10, 19, 30, 39, 12, 14, 28, 32, 8, 19, 31, 37, 11, 14, 25, 35, 9, 17, 26, 40, 9, 15, 32, 36, 9, 16, 26, 32, 10, 14, 28, 46, 9, 12, 23, 37, 8, 13, 28, 30, 9, 14, 23, 32, 9, 12, 27, 36, 9, 14, 22, 33, 10, 17, 26, 32, 10, 13, 23, 35, 9, 12, 21, 31, 597, 646, 643, 627, 575, 569, 681, 610, 17, 25, 36, 59, 14, 23, 26, 49, 9, 16, 23, 47, 14, 16, 31, 30, 9, 20, 32, 34, 11, 14, 31, 29, 10, 14, 26, 43, 10, 16, 28, 34, 9, 19, 29, 34, 9, 14, 23, 35, 13, 13, 25, 33, 9, 15, 28, 88, 10, 16, 21, 33, 9, 14, 22, 30, 12, 13, 25, 31, 9, 14, 26, 32, 10, 13, 25, 31, 8, 15, 29, 40, 10, 22, 107, 44, 9, 15, 29, 34, 9, 15, 28, 42, 8, 13, 24, 49, 7, 18, 29, 46, 9, 13, 23, 36, 9, 14, 20, 38, 8, 13, 24, 34, 9, 13, 21, 28, 11, 15, 21, 35, 9, 13, 22, 28, 10, 14, 22, 31, 8, 16, 26, 37, 9, 13, 21, 33, 8, 12, 21, 41, 8, 14, 26, 31, 8, 14, 26, 32, 8, 13, 29, 28, 11, 15, 25, 32, 9, 11, 20, 29, 8, 11, 22, 29, 9, 13, 25, 31, 7, 12, 21, 37, 8, 14, 22, 30, 8, 13, 20, 30, 8, 14, 20, 29, 7, 13, 26, 29, 8, 11, 20, 29, 8, 13, 24, 28, 10, 12, 21, 31, 8, 12, 21, 28, 9, 13, 21, 29, 8, 11, 21, 35, 555, 486, 543, 499, 19, 26, 47, 61, 11, 17, 29, 41, 11, 16, 27, 44, 11, 18, 26, 42, 11, 16, 36, 40, 11, 16, 26, 36, 10, 20, 24, 36, 14, 15, 25, 33, 9, 18, 23, 43, 9, 16, 25, 34, 11, 15, 23, 31, 14, 16, 29, 67, 9, 13, 28, 32, 9, 14, 23, 34, 8, 14, 22, 36, 10, 14, 29, 33, 8, 14, 26, 34, 10, 12, 24, 38, 8, 19, 23, 31, 9, 15, 36, 32, 11, 20, 25, 38, 9, 20, 30, 46, 7, 15, 23, 33, 9, 14, 24, 33, 7, 13, 27, 42, 7, 18, 21, 36, 8, 15, 24, 32, 9, 15, 29, 33, 14, 14, 23, 37, 33, 9, 14, 20, 33, 697, 519, 576, 514, 16, 26, 36, 51, 10, 16, 28, 47, 10, 14, 25, 44, 10, 16, 24, 43, 14, 21, 29, 45, 10, 16, 29, 43, 9, 20, 28, 40, 10, 13, 25, 33, 11, 13, 29, 37, 10, 13, 24, 36, 8, 12, 25, 33, 10, 15, 23, 34, 7, 12, 24, 33, 9, 13, 26, 32, 8, 16, 25, 35, 9, 13, 154, 31, 7, 16, 25, 35, 9, 12, 23, 41, 7, 13, 23, 32, 8, 13, 25, 33, 8, 16, 30, 40, 9, 13, 26, 42, 9, 12, 23, 32, 10, 14, 23, 35, 7, 11, 22, 30, 10, 12, 28, 32, 9, 14, 25, 35, 11, 15, 36, 35, 11, 23, 24, 34, 37, 13, 24, 30, 8, 12, 25, 32, 8, 12, 23, 32, 8, 11, 31, 30, 9, 14, 39, 32, 7, 11, 22, 37, 9, 11, 30, 33, 7, 12, 20, 33, 8, 21, 23, 30, 8, 11, 23, 37, 19, 12, 21, 29, 9, 14, 22, 29, 9, 13, 24, 37, 7, 11, 24, 33, 10, 12, 24, 35, 7, 13, 23, 33, 8, 11, 30, 32, 8, 11, 25, 33, 9, 14, 24, 35, 7, 11, 22, 35, 487, 491, 612, 790, 16, 25, 50, 61, 10, 16, 31, 46, 9, 15, 24, 45, 9, 17, 24, 46, 8, 17, 25, 41, 10, 16, 27, 36, 10, 13, 23, 39, 11, 15, 27, 33, 10, 15, 25, 45, 8, 12, 25, 37, 8, 14, 24, 30, 10, 13, 26, 36, 9, 12, 23, 40, 8, 15, 29, 40, 7, 14, 23, 31, 10, 15, 25, 31, 7, 14, 28, 32, 9, 13, 23, 35, 6, 20, 28, 31, 8, 13, 23, 33, 8, 14, 31, 35, 9, 13, 28, 32, 7, 13, 28, 36, 8, 13, 32, 32, 7, 13, 21, 31, 8, 16, 24, 35, 13, 13, 23, 37, 8, 12, 22, 32, 7, 12, 23, 32, 9, 13, 24, 33, 11, 12, 24, 32, 8, 12, 26, 37, 7, 14, 27, 31, 8, 12, 25, 30, 7, 11, 20, 31, 9, 12, 25, 39, 7, 13, 23, 37, 10, 14, 26, 38, 15, 12, 23, 33, 7, 13, 22, 30, 7, 10, 21, 34, 8, 13, 40, 38, 6, 11, 24, 35, 9, 11, 23, 32, 7, 11, 20, 32, 8, 11, 23, 32, 8, 11, 23, 30, 10, 15, 23, 31, 8, 11, 20, 31, 510, 534, 598, 605, 18, 20, 41, 87, 10, 24, 28, 43, 13, 14, 31, 43, 10, 16, 28, 40, 11, 20, 28, 49, 13, 14, 24, 37, 11, 19, 41, 47, 9, 16, 29, 35, 13, 16, 29, 34, 11, 17, 26, 47, 9, 17, 28, 39, 11, 13, 26, 37, 9, 13, 24, 35, 8, 14, 26, 41, 8, 17, 28, 36, 9, 18, 155, 34, 8, 14, 23, 34, 10, 13, 27, 43, 10, 147, 25, 36, 9, 14, 26, 42, 12, 19, 32, 70, 12, 19, 30, 39, 7, 14, 26, 33, 10, 16, 32, 35, 11, 13, 25, 32, 8, 14, 22, 34, 6, 12, 23, 33, 8, 14, 33, 32, 9, 12, 20, 32, 9, 16, 23, 31, 6, 13, 27, 38, 10, 13, 22, 34, 8, 13, 21, 33, 9, 11, 25, 33, 7, 12, 23, 34, 9, 12, 24, 37, 8, 14, 21, 33, 9, 13, 25, 31, 7, 13, 20, 35, 7, 13, 29, 31, 7, 11, 21, 32, 9, 13, 23, 42, 10, 13, 27, 44, 8, 18, 24, 31, 7, 12, 22, 32, 8, 13, 23, 31, 9, 12, 20, 32, 8, 13, 23, 32, 7, 11, 21, 31, 597, 596, 553, 562, 17, 22, 44, 77, 10, 15, 31, 49, 10, 16, 26, 45, 8, 15, 30, 38, 11, 15, 28, 46, 11, 16, 26, 51, 12, 15, 25, 44, 12, 17, 30, 36, 11, 16, 34, 48, 8, 18, 24, 38, 9, 14, 28, 43, 9, 15, 28, 35, 9, 15, 28, 34, 9, 15, 26, 38, 9, 14, 23, 45, 10, 15, 150, 31, 9, 15, 23, 40, 9, 15, 29, 36, 7, 14, 32, 41, 9, 20, 24, 38, 8, 14, 32, 66, 10, 14, 24, 36, 9, 12, 27, 35, 9, 14, 24, 39, 7, 12, 22, 32, 8, 13, 24, 33, 7, 13, 22, 33, 10, 12, 23, 33, 8, 13, 21, 35, 9, 14, 23, 33, 7, 13, 23, 33, 8, 16, 26, 37, 10, 16, 25, 37, 8, 14, 24, 35, 7, 13, 21, 32, 11, 14, 29, 37, 9, 13, 25, 30, 8, 16, 26, 39, 7, 13, 20, 40, 8, 13, 28, 35, 8, 13, 21, 34, 7, 14, 25, 35, 8, 11, 22, 40, 7, 13, 22, 30, 9, 11, 22, 37, 8, 15, 21, 32, 8, 12, 21, 34, 10, 12, 23, 31, 8, 13, 20, 36, 528, 556, 563, 509, 18, 24, 46, 62, 11, 15, 32, 40, 10, 14, 27, 42, 10, 16, 28, 43, 10, 14, 32, 43, 11, 14, 26, 40, 9, 16, 30, 42, 10, 14, 23, 38, 9, 13, 31, 33, 10, 14, 23, 33, 9, 15, 23, 33, 9, 14, 23, 38, 7, 12, 24, 36, 8, 12, 23, 33, 8, 13, 25, 37, 9, 13, 22, 35, 8, 11, 22, 53, 8, 19, 25, 45, 8, 11, 34, 33, 9, 17, 23, 35, 9, 16, 28, 39, 9, 15, 27, 40, 12, 15, 25, 30, 11, 13, 24, 38, 7, 12, 22, 33, 7, 12, 24, 32, 9, 10, 21, 31, 7, 12, 22, 31, 7, 14, 20, 30, 9, 12, 22, 31, 8, 12, 23, 28, 7, 13, 25, 39, 8, 12, 20, 30, 9, 12, 22, 34, 7, 11, 22, 31, 8, 12, 23, 41, 7, 18, 25, 32, 9, 12, 24, 31, 7, 20, 20, 33, 12, 12, 21, 37, 7, 11, 23, 30, 9, 13, 26, 33, 8, 11, 22, 40, 8, 10, 23, 33, 8, 13, 20, 31, 9, 14, 22, 29, 7, 12, 25, 31, 9, 13, 22, 30, 9, 12, 19, 32, 496, 525, 555, 560, 16, 21, 42, 54, 11, 15, 31, 35, 11, 17, 28, 48, 14, 18, 30, 47, 10, 15, 22, 41, 10, 15, 24, 42, 10, 15, 26, 63, 9, 14, 23, 32, 10, 19, 31, 31, 8, 13, 21, 39, 8, 12, 24, 38, 9, 12, 22, 31, 8, 17, 20, 33, 8, 13, 23, 31, 8, 12, 21, 29, 11, 16, 29, 32, 11, 18, 20, 34, 8, 16, 23, 40, 9, 13, 21, 33, 10, 13, 21, 32, 11, 15, 26, 33, 8, 13, 22, 32, 11, 15, 30, 39, 9, 13, 21, 29, 8, 11, 21, 30, 8, 14, 19, 32, 9, 11, 20, 28, 10, 12, 28, 28, 8, 10, 20, 29, 9, 11, 20, 42, 8, 11, 21, 29, 10, 12, 22, 30, 8, 11, 19, 29, 11, 17, 21, 38, 8, 13, 19, 29, 8, 11, 21, 29, 7, 11, 21, 28, 8, 12, 22, 30, 7, 11, 21, 31, 7, 12, 21, 31, 7, 10, 21, 34, 7, 13, 21, 30, 7, 11, 21, 31, 8, 12, 22, 32, 7, 12, 20, 32, 8, 17, 35, 37, 7, 13, 21, 29, 8, 12, 21, 31, 10, 11, 25, 27, 539, 597, 504, 585, 17, 27, 58, 80, 10, 16, 28, 37, 13, 18, 26, 40, 10, 17, 25, 40, 10, 15, 24, 36, 9, 15, 30, 37, 12, 14, 26, 49, 9, 14, 26, 38, 8, 14, 23, 35, 8, 16, 26, 41, 11, 14, 25, 33, 11, 15, 24, 35, 10, 14, 23, 35, 9, 16, 23, 39, 9, 16, 31, 45, 8, 15, 27, 36, 9, 14, 23, 32, 8, 14, 23, 45, 10, 14, 26, 37, 7, 13, 22, 33, 8, 108, 23, 35, 10, 18, 22, 31, 9, 14, 22, 37, 7, 12, 22, 33, 9, 13, 22, 30, 8, 15, 23, 38, 9, 13, 22, 31, 9, 15, 31, 33, 8, 12, 21, 31, 7, 13, 21, 32, 8, 13, 24, 31, 9, 13, 22, 38, 8, 16, 30, 41, 7, 12, 24, 32, 9, 12, 22, 32, 7, 19, 31, 32, 8, 18, 24, 31, 17, 21, 24, 36, 7, 12, 21, 44, 7, 12, 23, 32, 9, 11, 22, 31, 6, 14, 22, 33, 8, 11, 21, 36, 9, 17, 24, 36, 7, 11, 23, 30, 8, 12, 21, 38, 7, 11, 21, 34, 9, 15, 29, 31, 10, 13, 20, 33, 7, 14, 25, 31, 521, 523, 555, 537, 16, 21, 50, 71, 10, 20, 31, 40, 10, 15, 31, 47, 11, 16, 27, 40, 10, 13, 27, 45, 9, 15, 35, 44, 11, 19, 27, 36, 10, 13, 27, 34, 9, 13, 22, 35, 10, 13, 25, 32, 8, 12, 23, 33, 10, 13, 36, 33, 7, 11, 21, 35, 9, 14, 25, 33, 7, 14, 25, 40, 8, 19, 149, 36, 8, 12, 23, 44, 8, 15, 24, 47, 9, 14, 26, 36, 10, 13, 23, 33, 8, 14, 32, 43, 9, 18, 27, 34, 8, 12, 24, 42, 8, 13, 25, 31, 7, 11, 22, 34, 8, 13, 24, 36, 7, 11, 20, 36, 7, 12, 19, 33, 8, 16, 24, 32, 8, 13, 24, 35, 7, 11, 22, 34, 8, 13, 29, 35, 7, 10, 20, 33, 7, 12, 33, 31, 7, 11, 25, 30, 8, 12, 22, 32, 7, 11, 23, 40, 9, 12, 23, 31, 9, 10, 23, 34, 8, 19, 25, 32, 8, 12, 20, 31, 17, 12, 21, 35, 10, 15, 24, 38, 8, 13, 22, 30, 7, 14, 22, 29, 8, 13, 23, 32, 7, 11, 23, 35, 8, 14, 24, 32, 8, 11, 23, 29, 527, 579, 577, 609, 18, 21, 37, 72, 13, 16, 28, 40, 11, 18, 29, 41, 11, 17, 31, 47, 9, 16, 31, 47, 10, 19, 31, 40, 10, 14, 26, 41, 11, 16, 28, 33, 9, 14, 22, 35, 11, 13, 25, 38, 11, 15, 26, 38, 10, 14, 28, 39, 8, 13, 29, 39, 11, 16, 25, 42, 10, 17, 27, 41, 9, 13, 24, 33, 10, 12, 22, 33, 9, 14, 25, 43, 7, 12, 22, 35, 10, 125, 22, 30, 9, 20, 30, 69, 10, 14, 27, 45, 8, 16, 29, 36, 11, 13, 22, 35, 8, 12, 23, 32, 8, 13, 23, 35, 6, 13, 19, 31, 9, 13, 23, 34, 7, 14, 20, 32, 11, 12, 21, 33, 7, 13, 24, 31, 7, 15, 23, 33, 7, 12, 23, 32, 9, 14, 23, 31, 8, 12, 20, 30, 8, 14, 30, 36, 7, 12, 20, 45, 9, 13, 23, 34, 6, 12, 21, 30, 7, 13, 25, 35, 9, 13, 23, 33, 11, 14, 23, 32, 7, 11, 30, 33, 8, 12, 20, 31, 7, 12, 21, 37, 9, 10, 23, 31, 8, 11, 21, 33, 7, 13, 22, 33, 7, 12, 19, 35, 554, 574, 584, 547, 15, 25, 47, 87, 10, 18, 30, 64, 10, 17, 23, 40, 9, 15, 26, 63, 9, 14, 25, 38, 11, 16, 22, 34, 10, 15, 22, 43, 12, 14, 27, 36, 9, 16, 30, 42, 10, 19, 33, 32, 8, 14, 23, 33, 10, 14, 24, 32, 10, 14, 22, 33, 8, 13, 24, 34, 8, 14, 24, 34, 8, 13, 41, 39, 8, 14, 30, 41, 9, 13, 27, 38, 8, 13, 24, 31, 9, 13, 22, 31, 8, 13, 31, 45, 8, 103, 28, 37, 6, 20, 23, 30, 10, 15, 29, 36, 8, 12, 21, 33, 8, 12, 24, 32, 9, 12, 21, 32, 10, 12, 22, 33, 7, 11, 20, 31, 8, 13, 22, 34, 9, 13, 20, 33, 9, 12, 22, 38, 8, 12, 20, 31, 8, 13, 25, 36, 8, 12, 19, 31, 8, 16, 21, 32, 8, 11, 25, 32, 7, 13, 20, 33, 7, 19, 23, 31, 7, 11, 24, 30, 7, 12, 20, 33, 8, 13, 23, 37, 8, 10, 30, 35, 8, 12, 22, 31, 8, 11, 22, 33, 7, 14, 27, 37, 7, 16, 21, 32, 9, 11, 23, 37, 8, 15, 21, 34, 569, 597, 603, 622, 16, 23, 63, 58, 9, 16, 29, 40, 16, 18, 29, 40, 10, 16, 25, 48, 15, 15, 26, 47, 9, 15, 25, 39, 10, 16, 35, 50, 11, 16, 25, 39, 9, 19, 27, 45, 10, 16, 29, 41, 10, 15, 26, 46, 12, 14, 25, 35, 7, 14, 23, 33, 8, 16, 37, 40, 8, 14, 24, 33, 9, 15, 28, 35, 8, 19, 24, 46, 11, 14, 27, 42, 9, 14, 27, 33, 8, 15, 23, 37, 9, 18, 33, 37, 10, 14, 28, 41, 7, 12, 29, 31, 9, 14, 24, 41, 7, 11, 24, 32, 8, 14, 22, 33, 10, 13, 30, 36, 10, 12, 22, 31, 6, 14, 25, 37, 8, 11, 24, 34, 10, 18, 27, 36, 8, 16, 32, 39, 7, 16, 31, 32, 9, 14, 22, 31, 11, 18, 25, 34, 9, 13, 26, 37, 8, 12, 21, 34, 11, 14, 23, 33, 8, 11, 25, 34, 8, 13, 23, 36, 6, 13, 22, 31, 8, 16, 21, 47, 7, 16, 23, 39, 9, 15, 21, 34, 6, 11, 28, 30, 7, 12, 21, 36, 6, 11, 22, 31, 9, 12, 22, 29, 7, 13, 21, 30};
	double [] data_iterations=new double [] {175, 120, 108, 117, 112, 98, 99, 93, 105, 95, 95, 96, 91, 91, 92, 90, 100, 98, 95, 94, 109, 83, 84, 84, 79, 80, 80, 82, 78, 80, 79, 84, 76, 74, 69, 73, 73, 92, 73, 77, 71, 77, 71, 70, 78, 72, 73, 78, 73, 140, 97, 93, 91, 99, 95, 95, 84, 84, 87, 80, 90, 86, 78, 82, 81, 84, 90, 83, 80, 88, 107, 89, 76, 78, 70, 75, 85, 76, 76, 147, 108, 105, 100, 94, 96, 82, 85, 96, 87, 94, 88, 93, 93, 98, 86, 95, 85, 92, 92, 83, 98, 81, 79, 78, 84, 78, 85, 81, 73, 137, 112, 95, 91, 95, 85, 93, 88, 91, 81, 84, 140, 80, 75, 81, 81, 79, 92, 183, 87, 94, 94, 100, 81, 81, 79, 71, 82, 72, 77, 87, 76, 82, 79, 80, 78, 83, 69, 70, 78, 77, 74, 71, 71, 75, 68, 73, 74, 69, 72, 75, 153, 98, 98, 97, 103, 89, 90, 87, 93, 84, 80, 126, 82, 80, 80, 86, 82, 84, 81, 92, 94, 105, 78, 80, 89, 82, 79, 86, 88, 76, 129, 101, 93, 93, 109, 98, 97, 81, 90, 83, 78, 82, 76, 80, 84, 207, 83, 85, 75, 79, 94, 90, 76, 82, 70, 82, 83, 97, 92, 104, 77, 75, 80, 94, 77, 83, 72, 82, 79, 81, 74, 83, 75, 81, 76, 81, 77, 82, 75, 152, 103, 93, 96, 91, 89, 85, 86, 95, 82, 76, 85, 84, 92, 75, 81, 81, 80, 85, 77, 88, 82, 84, 85, 72, 83, 86, 74, 74, 79, 79, 83, 79, 75, 69, 85, 80, 88, 83, 72, 72, 99, 76, 75, 70, 74, 72, 79, 70, 166, 105, 101, 94, 108, 88, 118, 89, 92, 101, 93, 87, 81, 89, 89, 216, 79, 93, 218, 91, 133, 100, 80, 93, 81, 78, 74, 87, 73, 79, 84, 79, 75, 78, 76, 82, 76, 78, 75, 80, 71, 87, 94, 81, 73, 75, 73, 76, 70, 160, 105, 97, 91, 100, 104, 96, 95, 109, 88, 94, 87, 86, 88, 91, 206, 87, 89, 94, 91, 120, 84, 83, 86, 73, 78, 75, 78, 77, 79, 76, 87, 88, 81, 73, 91, 77, 89, 80, 84, 76, 81, 81, 72, 79, 76, 75, 76, 77, 150, 98, 93, 97, 99, 91, 97, 85, 86, 80, 80, 84, 79, 76, 83, 79, 94, 97, 86, 84, 92, 91, 82, 86, 74, 75, 71, 72, 71, 74, 71, 84, 70, 77, 71, 84, 82, 76, 80, 82, 71, 81, 81, 74, 72, 74, 75, 74, 72, 133, 92, 104, 109, 88, 91, 114, 78, 91, 81, 82, 74, 78, 75, 70, 88, 83, 87, 76, 76, 85, 75, 95, 72, 70, 73, 68, 78, 67, 82, 69, 74, 67, 87, 69, 69, 67, 72, 70, 71, 72, 71, 70, 74, 71, 97, 70, 72, 73, 182, 91, 97, 92, 85, 91, 101, 87, 80, 91, 83, 85, 82, 87, 101, 86, 78, 90, 87, 75, 174, 81, 82, 74, 74, 84, 75, 88, 72, 73, 76, 82, 95, 75, 75, 89, 81, 98, 84, 74, 73, 75, 76, 86, 71, 79, 73, 84, 76, 77, 158, 101, 103, 94, 95, 103, 93, 84, 79, 80, 76, 92, 74, 81, 86, 212, 87, 94, 85, 79, 97, 88, 86, 77, 74, 81, 74, 71, 80, 80, 74, 85, 70, 83, 73, 74, 81, 75, 76, 84, 71, 85, 87, 73, 72, 76, 76, 78, 71, 148, 97, 99, 106, 103, 100, 91, 88, 80, 87, 90, 91, 89, 94, 95, 79, 77, 91, 76, 187, 128, 96, 89, 81, 75, 79, 69, 79, 73, 77, 75, 78, 74, 77, 70, 88, 84, 79, 69, 80, 78, 80, 81, 71, 77, 73, 73, 75, 73, 174, 122, 90, 113, 86, 83, 90, 89, 97, 94, 78, 80, 79, 79, 80, 101, 93, 87, 76, 75, 97, 176, 79, 90, 74, 76, 74, 77, 69, 77, 75, 81, 71, 82, 70, 77, 76, 73, 80, 72, 72, 81, 83, 73, 74, 85, 76, 80, 78, 160, 94, 103, 99, 103, 88, 111, 91, 100, 96, 97, 86, 77, 101, 79, 87, 97, 94, 83, 83, 97, 93, 79, 88, 74, 77, 89, 75, 82, 77, 91, 95, 86, 76, 88, 85, 75, 81, 78, 80, 72, 92, 85, 79, 75, 76, 70, 72, 71, 117, 98, 97, 94, 99, 101, 89, 90, 101, 100, 88, 86, 80, 89, 80, 186, 86, 92, 74, 90, 87, 85, 76, 87, 74, 79, 78, 85, 73, 75, 77, 78, 76, 75, 70, 85, 75, 82, 71, 71, 68, 77, 74, 74, 73, 73, 68, 71, 73, 411, 115, 98, 93, 98, 102, 107, 92, 84, 100, 84, 104, 80, 100, 83, 95, 87, 91, 78, 86, 109, 92, 86, 90, 75, 90, 84, 93, 80, 77, 84, 79, 92, 92, 72, 77, 87, 98, 94, 90, 80, 90, 86, 79, 78, 92, 80, 75, 80};
	LogNormalDistribution lnd= new LogNormalDistribution(data_iterations);
	System.out.println(lnd.toString());
	//System.out.println(lnd.L);
	System.out.println(LogNormalGenerated(3.0604,0.7907));
	double constant=1;
	int	 iterations=50;
	String file_name="9-2.csv";
	double mu=9;
	double sigma=2;
	//simulate_delay_Considering_z_negative_csv(data_iterations, iterations, constant, mu, sigma, file_name);//Adding 4 delays
	simulate_delay_Z_data(295,data_iterations, iterations, constant, mu, sigma, file_name);
	}
}
