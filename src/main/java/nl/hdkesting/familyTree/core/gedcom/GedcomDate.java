package nl.hdkesting.familyTree.core.gedcom;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for the (possibly incomplete) dates that appear in a GEDCOM file.
 * It ignores the possibility of non-Gregorian calendars.
 */
public class GedcomDate {
    private static final Pattern dmyPattern = Pattern.compile("\\d{1,2} [A-Za-z]{3} \\d{4}");
    private static final Pattern myPattern = Pattern.compile("[A-Za-z]{3} \\d{4}");
    private static final Pattern yPattern = Pattern.compile("\\d{4}");
    private static final String[] monthNamesEn = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static final String[] monthNamesNl = {"Jan", "Feb", "Mrt", "Apr", "Mei", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};

    private LocalDate date;
    private boolean isExact;

    /**
     * Private constructor, accessed through factory method {@link GedcomDate#parse}.
     * @param date - the parsed date
     * @param isExact - whether the date was specified fully (day-month-year)
     */
    private GedcomDate(LocalDate date, boolean isExact) {
        this.date = date;
        this.isExact = isExact;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isExact() {
        return isExact;
    }

    public static GedcomDate parse(String value) {
        // try and find "dd MMM yyyy" -> exact match
        // then try "MMM yyyy" -> use day 1, not exact
        // then try "yyyy" -> use jan 1, not exact
        // else null.
        // ignore "about" or "before" markers

        Matcher m = dmyPattern.matcher(value);
        if (m.find()) {
            // exact date
            String[] parts = value.substring(m.start(), m.end()).split(" ");
            return new GedcomDate(getDate(parts[0], parts[1], parts[2]), true);
        }

        m = myPattern.matcher(value);
        if (m.find()) {
            // inexact date: month + year
            String[] parts = value.substring(m.start(), m.end()).split(" ");
            return new GedcomDate(getDate("1", parts[0], parts[1]), false);
        }

        m = yPattern.matcher(value);
        if (m.find()) {
            // inexact date: year
            String[] parts = value.substring(m.start(), m.end()).split(" ");
            return new GedcomDate(getDate("1", monthNamesEn[0], parts[0]), false);
        }

        return null;
    }

    private static LocalDate getDate(String day, String month, String year) {
        int iday = Integer.parseInt(day, 10);
        int imonthEn = getArrayIndex(monthNamesEn, month)+1;
        int imonthNl = getArrayIndex(monthNamesNl, month)+1;
        int imonth = imonthEn > 0 ? imonthEn : imonthNl > 0 ? imonthNl : 1;
        int iyear = Integer.parseInt(year);

        return LocalDate.of(iyear, imonth, iday);
    }

    private static int getArrayIndex(String[] arr, String value) {
        for(int i=0; i<arr.length; i++){
            if(arr[i].equalsIgnoreCase(value)){
                return i;
            }
        }
        return -1;
    }
}
