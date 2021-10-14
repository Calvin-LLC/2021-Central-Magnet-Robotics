package com.example.lib;
import java.util.Random;

public class central {
    public static void main(String[] args) {
        /* Data Types you will use */
        boolean b_value = true;         // true or false, 1 or 0
        int     i_value = 1;            // whole numbers
        long    l_value = 1000000000;   // longer int
        double  d_value = .011;         // double is double sized floats (use for decimals that floats cant do)
        float   f_value = .01f;         // f at the end declares the value as a float, rather than a double


        /* Operators */
        int i_number = 10;
        int i_number_2 = 2;

        int i_temp = i_number + i_number_2; // results in 12
        i_number += i_number_2;             // will set i_number to i_number + i_number_2

        int i_temp2 = i_number_2 - i_number; // 8
        i_number -= i_number_2;              // sets i_number to i_number - i_number2;

        int i_temp3 = i_number / i_number_2; // 5
        i_number /= i_number_2;              // will set i_number to i_number / i_number_2

        int i_temp4 = i_number * i_number_2; // 20
        i_number *= i_number_2;              // sets i_number to i_number * i_number_2

        int i_temp5 = i_number % i_number_2; // 0, (number is even)
        i_number++; // adds one to i_number, it's the equivalent to i_number+=1
        i_number--; // subtracts one to i_number



        /* Conditionals */
        if (true) {         // if boolean value is true (uses == instead of =)
            // do something
        } else if (true) {  // if the first if isn't true it will be a second if statement
            // do something
        } else {            // if all else fails, run this :kekw:

        }

        while (true) {      // while condition is true, it will repeat the code
            // do something
        }

        /* first value is ran once at the start of the loop (used for creating variables)
           second value is the conditional, basically it will run as long as this condition is met
           third value is ran once at the end of each loop
         */
        for (int i = 0; i < 10; i++) {

        }

        switch(i_number) {
            case 10:
                // do stuff
                break;
            case 15:
                // do stuff2
                break;
            default:
                // do more stuff
                break;
        }


        // example
        int max_val = 15;
        for (int i = 0; i < max_val; i++) {
            System.out.println("hello world #" + i);
        }

        /* random  functions & usage */
        Random rand = new Random();                    // creates "rand" object
        int     i_random = rand.nextInt(100);   // number between 0-99
        double  d_random = rand.nextDouble() * 100.0; // generates a random double between 0-100
        double  g_random = rand.nextGaussian() * 100; // generates random positive or negative double

        // example:
        if (rand.nextInt(100) < 50) {         // if random integer is less than 50, it will run the statement
            System.out.println("wow conditionals"); // prints a line
        }

        for (int i = 0; i < 100000; i++) {
            if (i % 3 == 0) {
                if(i % 5 == 0) {
                    System.out.println("FizzBuzz");
                }
                System.out.println("Fizz");
            }
            else if (i % 5 == 0) System.out.println("Buzz");
            else System.out.println(i);
        }

    }
}
