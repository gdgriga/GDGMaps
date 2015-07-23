package lv.gdgriga.gdgmaps.tag;

import android.util.Log;

class Coordinate {
    private int degrees;
    private int minutes;
    private int seconds;

    static Coordinate fromDecimalDegrees(double decimalDegrees) {
        int realPart = (int) Math.floor(decimalDegrees);
        double fractionalPart = decimalDegrees - realPart;
        return calculateCoordinateForm(realPart, fractionalPart);
    }

    private static Coordinate calculateCoordinateForm(int realPart, double fractionalPart) {
        Coordinate coordinate = new Coordinate();
        coordinate.degrees = realPart;
        coordinate.minutes = (int) Math.floor(60 * fractionalPart);
        coordinate.seconds = (int) Math.floor(1e4 * 3600 * (fractionalPart - coordinate.minutes / 60.));
        Log.d("StoreTagTask", coordinate.toString());
        return coordinate;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(degrees)
                                  .append("/1,")
                                  .append(minutes)
                                  .append("/1,")
                                  .append(seconds)
                                  .append("/10000")
                                  .toString();
    }
}