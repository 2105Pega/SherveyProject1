function getCookie(name)
{
   // xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest'); xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
	
	let x = document.cookie.split(";");
	//console.log(x);
	for(var i = 0; i < x.length; i++)
	{
		let x2 = x[i].split("=");
		
		//console.log(x2 + "Name: " + name);
		
		if(x2[0].trim() == name.trim())
		{
			//console.log(name.trim() + "= " + x2[1].trim());
			
			return x2[1];
		}
	}
}

async function logInClick()
{
    let username = document.getElementById("Username").value;
    let password = document.getElementById("Password").value;

	let url = "http://localhost:8080/project1/api/client/userByUP?username=" + username + 
    "&password=" + password;

    console.log(url);
    
    
    let response = await fetch(url);

    if(response.ok)
    {
        let client = await response.json();
		
		document.cookie = "currentUserid=" + client.clientID;
        window.location.href = "http://localhost:8080/project1/HTML/ClientPage.html"
    }
}

async function onClientLoad()
{
	
	console.log("onClientLoad");
	
	let currentClient = getCookie("currentUserid");
	
	let url = "http://localhost:8080/project1/api/client/user?id=" + currentClient;
	let response = await fetch(url);

    if(response.ok)
    {
        let client = await response.json();
        
        console.log(client);
        
        document.getElementById("Username").innerHTML = "username:   " + client.userName;
        document.getElementById("Password").innerHTML = "password:   " + client.password;
        document.getElementById("fName").innerHTML =    "First Name: " + client.firstName;
        document.getElementById("lName").innerHTML =    "Last Name : " + client.lastName;
        document.getElementById("Address").innerHTML =  "Address:    " + client.address;
        document.getElementById("id").innerHTML =       "Client id:  " + client.clientID;
    }
    
    
    document.getElementById("withdraw").addEventListener('click', function(){
	document.cookie = "transActionType=" + event.target.id;

	});
    document.getElementById("deposit").addEventListener('click', function(){
	document.cookie = "transActionType=" + event.target.id;

	});
    document.getElementById("transfer").addEventListener('click',function(){
	document.cookie = "transActionType=" + event.target.id;;

	});
    
  	populateAccountTable();
}

async function getAllClients()
{
	let response = await fetch("http://localhost:8080/project1/api/client/getAllClients");
	if(response.ok)
    {
        let accountList = await response.json();
        
        return accountList;
        }
	
}

async function getAllAccounts()
{
	let response = await fetch("http://localhost:8080/project1/api/account/getAllAccounts");
	if(response.ok)
    {
        let accountList = await response.json();
        
        return accountList;
        }
}

async function getAccountsByClient()
{
	let response = await fetch("http://localhost:8080/project1/api/account/getAllAccountsByID?id=" + getCookie("currentUserid"));
	
	if(response.ok)
    {
        let accountList = await response.json();
        
        return accountList;
        }
}

async function getCoAccountsByClient()
{
	let response = await fetch("http://localhost:8080/project1/api/account/getCoAccounts?id=" + getCookie("currentUserid"));
	
	if(response.ok)
    {
        let accountList = await response.json();
        
        return accountList;
    }

}

async function populateAccountTable()
{
	let accountList = await getAccountsByClient();
	
	console.log(accountList);
	
	let table = document.getElementById("accountTable");
	
	for(let ix = 0; ix < accountList.length; ix++)
	{
		let row = table.insertRow();
		let cell1 = row.insertCell(0);
		let cell2 = row.insertCell(1);
		let cell3 = row.insertCell(2);
		let cell4 = row.insertCell(3);
		let cell5 = row.insertCell(4);
		
		cell1.innerHTML = accountList[ix].accountName;
		cell2.innerHTML = accountList[ix].balance;
		cell3.innerHTML = accountList[ix].account_ID;
		cell4.innerHTML = accountList[ix].status;
		cell5.innerHTML = "No";
	}
	
	accountList = await getCoAccountsByClient()
	for(let ix = 0; ix < accountList.length; ix++)
	{
		let row = table.insertRow();
		let cell1 = row.insertCell(0);
		let cell2 = row.insertCell(1);
		let cell3 = row.insertCell(2);
		let cell4 = row.insertCell(3);
		let cell5 = row.insertCell(4);
		
		cell1.innerHTML = accountList[ix].accountName;
		cell2.innerHTML = accountList[ix].balance;
		cell3.innerHTML = accountList[ix].account_ID;
		cell4.innerHTML = accountList[ix].status;
		cell5.innerHTML = "Yes";
	}
}

async function populateEmployeeTable()
{
	let accountList = await getAllAccounts();
	
	console.log(accountList);
	
	let table = document.getElementById("accountTable");
	
	for(let ix = 0; ix < accountList.length; ix++)
	{
		let row = table.insertRow();
		let cell1 = row.insertCell(0);
		let cell2 = row.insertCell(1);
		let cell3 = row.insertCell(2);
		let cell4 = row.insertCell(3);
		let cell5 = row.insertCell(4);
		
		cell1.innerHTML = accountList[ix].accountName;
		cell2.innerHTML = accountList[ix].balance;
		cell3.innerHTML = accountList[ix].account_ID;
		cell4.innerHTML = accountList[ix].status;
		cell5.innerHTML = accountList[ix].ownerID;
	}
	
	table = document.getElementById("clientTable");
	let clientList = await getAllClients();
	
	for(let ix = 0; ix < clientList.length; ix++)
	{
		let row = table.insertRow();
		let cell1 = row.insertCell(0);
		let cell2 = row.insertCell(1);
		let cell3 = row.insertCell(2);
		let cell4 = row.insertCell(3);
		let cell5 = row.insertCell(4);
		let cell6 = row.insertCell(5);
		
		cell1.innerHTML = clientList[ix].userName;
		cell2.innerHTML = clientList[ix].password;
		cell3.innerHTML = clientList[ix].clientID;
		cell4.innerHTML = clientList[ix].firstName;
		cell5.innerHTML = clientList[ix].lastName;
		cell6.innerHTML = clientList[ix].address;
	}
	
}

function cleanTable(tableName)
{
	let table = document.getElementById(tableName);
	while(table.rows.length > 1)
	{
		table.deleteRow(1);
	}
}

function ResetTables()
{
	cleanTable("accountTable");
	cleanTable("clientTable");
	
	populateEmployeeTable(); 
}

async function submitTransaction()
{
	//console.log(document.cookie);
	
	let type = getCookie("transActionType");
	let cID = getCookie("currentUserid");
	let id = document.getElementById("selectedAccount").value;
	let amount  = document.getElementById("amountEntry").value;
	let url;
	
	console.log(cID);
	
	if(type == "withdraw")
	{
		//console.log("Withdraw " + amount + " for " + id);
		url = "http://localhost:8080/project1/api/account/withdraw?id=" + id +"&amount=" + amount + "&cID=" + cID;
		console.log(url);
		await fetch(url, {method : "POST"});
	}
	if(type == "deposit")
	{
		//console.log("Deposit" + amount + " for " + id);
		url = "http://localhost:8080/project1/api/account/deposit?id=" + id +"&amount=" + amount + "&cID=" + cID;
		await fetch(url, {method : "POST"});
	}
	if(type == "transfer")
	{
		let target = document.getElementById("targetSelect").value;
		//console.log("Transfer" + amount + " for " + id + " to " + target);
		
		url = "http://localhost:8080/project1/api/account/transfer?target=" +target +"&sender=" + id +"&amount=" + amount + "&cID=" + cID;
		await fetch(url, {method : "POST"});
	}
	
	document.getElementById("amountEntry").value = 0;
	
	cleanTable("accountTable");
	populateAccountTable();
}

async function createAccount()
{
	let name = document.getElementById("createAccountName").value;
	let id = getCookie("currentUserid");
	name.trim();
	let url = "http://localhost:8080/project1/api/account/makeAccount?id=" + id + "&accountName=" + name;
	
	let response = await fetch(url, {method: "POST"});
	
	if(response.ok)
	{
		cleanTable("accountTable");
		populateAccountTable();
	}
	else
	{
		alert("A Error Occurred.")
	}
}

function goToCreateClient()
{
	window.location.href = "http://localhost:8080/project1/HTML/CreateAccount.html";
}

async function createClient()
{
	/*	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String address;
	*/
	
	let newClient = {
		userName : document.getElementById("Username").value,
		password : document.getElementById("Password").value,
	 	firstName : document.getElementById("fName").value,
	 	lastName : document.getElementById("lName").value,
	 	address : document.getElementById("Address").value
	}
	
	let nC = JSON.stringify(newClient);
	
	console.log(nC);
	//makeUser
	let url = "http://localhost:8080/project1/api/client/makeUser";
	
	let response = await fetch(url, {method: "POST", headers: {"Content-Type": "application/json"}, body: nC})
	if(response.ok)
	{
		window.alert("Account Created. Please Log In");
		window.location.href = "http://localhost:8080/project1/HTML/LogIn.html";
	}
}

function toggleHideEditAcc()
{
	//console.log(document.getElementById("editAccount").style.display);
	if(document.getElementById("editAccount").style.display == "none")
	{
		document.getElementById("editAccount").style.display = "block";
	}
	else
	{
		document.getElementById("editAccount").style.display = "none";
	}
}

function toggleHideEditCli()
{
	//console.log(document.getElementById("editClient").style.display);
	if(document.getElementById("editClient").style.display == "none")
	{
		document.getElementById("editClient").style.display = "block";
	}
	else
	{
		document.getElementById("editClient").style.display = "none";
	}
}

async function prepEditClient()
{
	/*	 	<label>Input Client ID: </label> <input type="number" id="clientToUpdate" onchange="prepEditClient()"><br>
	 	<label>Username: </label> <input type="text" id="usernameUpdate"> <br>
	 	<label>Password: </label> <input type="text" id="passwordupdate"> <br>
	 	<label>First Name: </label> <input type="text" id="fNameUpdate"> <br>
	 	<label>Last Name: </label> <input type="text" id="lNameUpdate"> <br>
	 	<label>Address: </label> <input type="text" id="addresspdate"> <br>
	 	*/
	 	
	 	let url = "http://localhost:8080/project1/api/client/user?id=" + document.getElementById("clientToUpdate").value;
	 	
	 	let response = await fetch(url);
	 	
	 	if(response.ok)
	 	{
		let client = await response.json();
        
        console.log(client);

        document.getElementById("usernameUpdate").value = client.userName;
        document.getElementById("passwordupdate").value =  client.password;
        document.getElementById("fNameUpdate").value = client.firstName;
        document.getElementById("lNameUpdate").value = client.lastName;
        document.getElementById("addresspdate").value = client.address;



		}
}

async function updateClient()
{	 	
	 		let newClient = {
		
		userName : document.getElementById("usernameUpdate").value,
		password : document.getElementById("passwordupdate").value,
	 	firstName : document.getElementById("fNameUpdate").value,
	 	lastName : document.getElementById("lNameUpdate").value,
	 	address : document.getElementById("addresspdate").value,
	 	clientID : document.getElementById("clientToUpdate").value
	}
	
	console.log(newClient);
	
	let nC = JSON.stringify(newClient);
	
	console.log(nC);
	//makeUser
	let url = "http://localhost:8080/project1/api/client/UpdateUser";
	
	let response = await fetch(url, {method: "POST", headers: {"Content-Type": "application/json"}, body: nC})
	if(response.ok)
	{
		window.alert("Account Updated. Reloading Tables");
		ResetTables();
	}
	
}

async function deleteClient()
{
	console.log(document.getElementById("confirmDeleteCheck").checked);
	
	
	if(document.getElementById("confirmDeleteCheck").checked == true)
	{
	let url = "http://localhost:8080/project1/api/client/removeUserByID?id=" + document.getElementById("clientToUpdate").value;
	
	let response = await fetch(url, {method: "DELETE"});
	
	if(response.ok)
	{
		ResetTables();
		document.getElementById("confirmDeleteCheck").checked="false";
	}
	}
}