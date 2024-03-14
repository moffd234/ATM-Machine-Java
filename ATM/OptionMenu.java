import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;


// TODO - Fix areas where we are referencing the full file path instead of a relative path
// TODO - public void logFileExists(int accNum){}
// TODO - public void createLogFile(int accNum){}
// TODO - public void readLogFile(int accNum){}

// Create a directory of logs
// Each user will have their own logFile in the directory which holds transaction logs
public class OptionMenu {
	Scanner menuInput = new Scanner(System.in);
	DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");
	HashMap<Integer, Account> data = new HashMap<Integer, Account>();

	public void getLogin() throws IOException {
		boolean end = false;
		int customerNumber = 0;
		int pinNumber = 0;
		while (!end) {
			try {

				// Get pin and account number
				System.out.print("\nEnter your customer number: ");
				customerNumber = menuInput.nextInt();
				System.out.print("\nEnter your PIN number: ");
				pinNumber = menuInput.nextInt();

				Iterator it = data.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					Account acc;
					if (isValidLogin(customerNumber, pinNumber)) {
						JSONObject accounts = readAccountsJson();
						JSONObject currentAccount = (JSONObject) accounts.get(String.valueOf(customerNumber));
						double savingsTotal = Double.parseDouble(String.valueOf(currentAccount.get("Savings Balance")));
						double checkingTotal = Double.parseDouble(String.valueOf(currentAccount.get("Checking Balance")));
						acc = new Account(customerNumber, pinNumber, checkingTotal, savingsTotal);
						getAccountType(acc);
						end = true;
						break;
					}
				}
				if (!end) {
					System.out.println("\nWrong Customer Number or Pin Number");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Character(s). Only Numbers.");
			}
		}
	}

	public void getAccountType(Account acc) {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nSelect the account you want to access: ");
				System.out.println(" Type 1 - Checking Account");
				System.out.println(" Type 2 - Savings Account");
				System.out.println(" Type 3 - View All Balances");
				System.out.println(" Type 4 - Exit");
				System.out.print("\nChoice: ");

				int selection = menuInput.nextInt();

				switch (selection) {
				case 1:
					getChecking(acc);
					break;
				case 2:
					getSaving(acc);
					break;
				case 3:
					viewAllBalances(acc);
				case 4:
					end = true;
					break;
				default:
					System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
	}

	public void getChecking(Account acc) {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nChecking Account: ");
				System.out.println(" Type 1 - View Balance");
				System.out.println(" Type 2 - Withdraw Funds");
				System.out.println(" Type 3 - Deposit Funds");
				System.out.println(" Type 4 - Transfer Funds");
				System.out.println(" Type 5 - Exit");
				System.out.print("\nChoice: ");

				int selection = menuInput.nextInt();

				switch (selection) {
				case 1:
					System.out.println("\nChecking Account Balance: " + moneyFormat.format(acc.getCheckingBalance()));
					break;
				case 2:
					acc.getCheckingWithdrawInput();
					break;
				case 3:
					acc.getCheckingDepositInput();
					break;

				case 4:
					acc.getTransferInput("Checking");
					break;
				case 5:
					end = true;
					break;
				default:
					System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
	}

	public void getSaving(Account acc) {
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nSavings Account: ");
				System.out.println(" Type 1 - View Balance");
				System.out.println(" Type 2 - Withdraw Funds");
				System.out.println(" Type 3 - Deposit Funds");
				System.out.println(" Type 4 - Transfer Funds");
				System.out.println(" Type 5 - Exit");
				System.out.print("Choice: ");
				int selection = menuInput.nextInt();
				switch (selection) {
				case 1:
					System.out.println("\nSavings Account Balance: " + moneyFormat.format(acc.getSavingBalance()));
					break;
				case 2:
					acc.getsavingWithdrawInput();
					break;
				case 3:
					acc.getSavingDepositInput();
					break;
				case 4:
					acc.getTransferInput("Savings");
					break;
				case 5:
					end = true;
					break;
				default:
					System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
	}
	public void viewAllBalances(Account acc){
		double checkBalance = acc.getCheckingBalance();
		double savingBalance = acc.getSavingBalance();
		System.out.println("\nChecking Account Balance: " + moneyFormat.format(checkBalance));
		System.out.println("\nSaving Account Balance: " + moneyFormat.format(savingBalance));
	}

	public void createAccount() throws IOException {
		int cst_no = 0;
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nEnter your customer number ");
				cst_no = menuInput.nextInt();
				Iterator it = data.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (!userAlreadyExists(cst_no)) {
						end = true;
					}
				}
				if (!end) {
					System.out.println("\nThis customer number is already registered");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
		System.out.println("\nEnter PIN to be registered");
		int pin = menuInput.nextInt();
		writeUserToFile(cst_no, pin);
		data.put(cst_no, new Account(cst_no, pin));
		System.out.println("\nYour new account has been successfully registered!");
		System.out.println("\nRedirecting to login.............");
		getLogin();
	}

	public void mainMenu() throws IOException {
		data.put(952141, new Account(952141, 191904, 1000, 5000));
		data.put(123, new Account(123, 123, 20000, 50000));
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\n Type 1 - Login");
				System.out.println(" Type 2 - Create Account");
				System.out.print("\nChoice: ");
				int choice = menuInput.nextInt();
				switch (choice) {
				case 1:
					getLogin();
					end = true;
					break;
				case 2:
					createAccount();
					end = true;
					break;
				default:
					System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
		System.out.println("\nThank You for using this ATM.\n");
		menuInput.close();
		System.exit(0);
	}

	public boolean userAlreadyExists(int accNum){
		try{
			JSONObject accountsData = readAccountsJson();
			// Check if the account number exists in the JSON Object
			if(accountsData.containsKey(String.valueOf(accNum))){
				return true;
			}

		}
		catch (FileNotFoundException e) {
			System.out.println("JSON File Not Found");
			return false;
        }
		return false;
    }

	public boolean isValidLogin(int accNum, int pin) throws FileNotFoundException {
		JSONObject accountsData = readAccountsJson();
		// Checks if account number is in the accountsData
		if(accountsData.containsKey(String.valueOf(accNum))){
			// Gets the JSON object of the account
			JSONObject accObject = (JSONObject) accountsData.get(String.valueOf(accNum));
			int actualPin = Integer.parseInt((String) accObject.get("AccountPin"));
            return actualPin == pin;
		}
		return false;
	}

	public void writeUserToFile(int accNum, int pin){
		try {
			JSONObject accountsData = readFullJson();

			// Create new JSON Object for the new account entry
			JSONObject newAccount = new JSONObject();
			newAccount.put("AccountID", String.valueOf(accNum));
			newAccount.put("Account Pin", String.valueOf(pin));
			newAccount.put("Savings Balance", "0"); // Each account starts with $0
			newAccount.put("Checking Balance", "0");

			// Add new account to accounts
			JSONObject accounts = (JSONObject) accountsData.get("Accounts");
			accounts.put(String.valueOf(accNum), newAccount);

			writeJsonFile(accountsData);

		}catch (IOException e) {

			//exception handling left as an exercise for the reader
			System.out.println("File Not Found");
		}
	}
	public JSONObject readAccountsJson() throws FileNotFoundException {
        JSONObject file =  (JSONObject) JSONValue.parse(new FileReader("/Users/dan/Dev/Zipcode/Week 2/ATM-Machine-Java/ATM/Accounts.json"));

		// Check if there is a key called Accounts
		if(file.containsKey("Accounts")){
			// Return the key & value of Accounts as a JSON Object
			return (JSONObject) file.get("Accounts");
		}
		return null;
	}

	public JSONObject readFullJson() throws FileNotFoundException {
		return (JSONObject)
				JSONValue.parse(new FileReader("/Users/dan/Dev/Zipcode/Week 2/ATM-Machine-Java/ATM/Accounts.json"));
	}

	public void writeJsonFile(JSONObject jsonData){
		try(FileWriter fileWriter = new FileWriter("/Users/dan/Dev/Zipcode/Week 2/ATM-Machine-Java/ATM/Accounts.json")){
			JSONValue.writeJSONString(jsonData, fileWriter);
			System.out.println("SUCCESSFUL ACCOUNT WRITE");
		} catch (IOException e){
			System.out.println("Error writing to JSON file");
		}
	}
}