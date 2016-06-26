package com.james.bluetoothreceiver;

import java.util.List;

/**
 * Created by james on 26/06/16.
 */
public enum MorseBit {
    LONG, SHORT;

    public static MorseBit instanceOf(double time) {
        if (time <= 0.7) {
            return SHORT;
        } else {
            return LONG;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case LONG:
                return "-";
            case SHORT:
                return ".";
            default:
                return "";
        }
    }

    public static char bitsToChar(List<MorseBit> bits) {
        String bitString = "";
        for (MorseBit m : bits) {
            bitString += m;
        }
        switch (bitString) {
            case (".-"):
                return 'A';
            case ("-..."):
                return 'B';
            case ("-.-."):
                return 'C';
            case ("-.."):
                return 'D';
            case ("."):
                return 'E';
            case ("..-."):
                return 'F';
            case ("--."):
                return 'G';
            case ("...."):
                return 'H';
            case (".."):
                return 'I';
            case (".---"):
                return 'J';
            case ("-.-"):
                return 'K';
            case (".-.."):
                return 'L';
            case ("--"):
                return 'M';
            case ("-."):
                return 'N';
            case ("---"):
                return 'O';
            case (".--."):
                return 'P';
            case ("--.-"):
                return 'Q';
            case (".-."):
                return 'R';
            case ("..."):
                return 'S';
            case ("-"):
                return 'T';
            case ("..-"):
                return 'U';
            case ("...-"):
                return 'V';
            case (".--"):
                return 'W';
            case ("-..-"):
                return 'X';
            case ("-.--"):
                return 'Y';
            case ("--.."):
                return 'Z';
            default:
                return '0';
        }
    }
}
