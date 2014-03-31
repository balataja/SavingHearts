package com.example.savinghearts.helpers;

public class CalculationsHelper {

	public static final double TARGET_MAX = 1.0;
	public static final double TARGET_85_PERCENT = 0.85;
	public static final double TARGET_50_PERCENT = 0.5;
	
	/**
	 * Returns the Heart Rates at various target levels based on Doctor Gulati's research
	 * @param age User's age in years
	 * @param hrTarget The target level desired. Look at CalculationsHelper.TARGET_*
	 * @return Heart rate target at specific level
	 */
	public static int getTargetHeartRateFromAge(int age, double hrTarget) { 
		
		/*
		 * From:
		 * 	Heart Rate Response to Exercise Stress Testing in Asymptomatic Women:
		 * 	Circulation: Journal of the American Heart Association, June 28
		 */
		double maxHR = 206 - (0.88 * age);
		
		if (hrTarget == TARGET_MAX) {
			
			return (int)(1.0 * maxHR);
			
		} else if (hrTarget == TARGET_85_PERCENT) {
			
			return (int)(0.85 * maxHR);
		
		} else if (hrTarget == TARGET_50_PERCENT) {
		
			return (int)(0.5 * maxHR);
		
		} else {
			
			return 0;
		}		
	}
	
	/**
	 * Returns the Predicted Exercise Capacity at various target levels based on Doctor Gulati's research
	 * @param age User's age in years
	 * @param targetLevel The target level desired. Look at CalculationsHelper.TARGET_*
	 * @return
	 */
	public static double getTargetPredictedExerciseCapacityFromAge(int age, double targetLevel) {
		
		/*
		 * From:
		 * 	The Prognostic Value of a Nomogram for Exercise Capacity in Women:
		 * 	New England Journal of Medicine, August 4th 2005
		 */
		double maxHR = 14.7 - (0.13 * age);
		
		if (targetLevel == TARGET_MAX) {
			
			return (1.0 * maxHR);
			
		} else if (targetLevel == TARGET_85_PERCENT) {
			
			return (0.85 * maxHR);
			
		} else if (targetLevel == TARGET_50_PERCENT) {
			
			return (0.50 * maxHR);
			
		} else {
			
			return 0.0;
		}
	}
	
	/**
	 * Returns the calorie count based on the user's weight and the MET*hours being converted.
	 * @param weight Weight of the user in pounds.
	 * @param metHours Number of MET-hours being converted.
	 * @return Calories burnt 
	 */
	public static double getCaloriesFromMetHours(int weight, double metHours) {
		
		double weightKilos = 0.453592 * weight; // convert to kilograms
		
		return Math.floor(weightKilos * metHours * 100.0)/100.0;
	}
	
	public static double getCaloriesFromMetMinutes(int weight, int metMinutes) {
		double hours = metMinutes/60.0;
		return getCaloriesFromMetHours(weight, hours);
	}
	
	
	
}
