package org.wora.utilitaire;
import java.time.LocalDate;
import java.util.function.Predicate;

public class ValidationUtils {


    public static Predicate<Double> isInRange(double minValue, double maxValue) {
        return (value) -> value != null && value >= minValue && value <= maxValue;
    }


    public static Predicate<LocalDate[]> isStartDateBeforeEndDate = (dates) ->
            dates[0] != null && dates[1] != null && dates[0].isBefore(dates[1]);


    public static Predicate<String> isValidEmail = (email) ->
            email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");


    public static Predicate<Double[]> isBudgetValid = (costs) ->
            costs[0] != null && costs[1] != null && costs[0] >= costs[1] + costs[2];


    public static Predicate<Integer> isProjectIdUnique = (projectId) -> {

        return true;

};
    }

