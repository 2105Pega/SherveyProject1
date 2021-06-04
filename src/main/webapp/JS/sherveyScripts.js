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

async function getAccountsByClient()
{
	let response = await fetch("http://localhost:8080/project1/api/account/getAllAccountsByID?id=" + getCookie("currentUserid"));
	
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
		
		cell1.innerHTML = accountList[ix].accountName;
		cell2.innerHTML = accountList[ix].balance;
		cell3.innerHTML = accountList[ix].account_ID;
		cell4.innerHTML = accountList[ix].status;
	}
}

function cleanTable()
{
	let table = document.getElementById("accountTable");
	while(table.rows.length > 1)
	{
		table.deleteRow(1);
	}
}

async function submitTransaction()
{
	//console.log(document.cookie);
	
	let type = getCookie("transActionType");
	let id = document.getElementById("selectedAccount").value;
	let amount  = document.getElementById("amountEntry").value;
	let url;
	if(type == "withdraw")
	{
		//console.log("Withdraw " + amount + " for " + id);
		url = "http://localhost:8080/project1/api/account/withdraw?id=" + id +"&amount=" + amount;
		await fetch(url, {method : "POST"});
	}
	if(type == "deposit")
	{
		//console.log("Deposit" + amount + " for " + id);
		url = "http://localhost:8080/project1/api/account/deposit?id=" + id +"&amount=" + amount;
		await fetch(url, {method : "POST"});
	}
	if(type == "transfer")
	{
		let target = document.getElementById("targetSelect").value;
		//console.log("Transfer" + amount + " for " + id + " to " + target);
		
		url = "http://localhost:8080/project1/api/account/transfer?target=" +target +"&sender=" + id +"&amount=" + amount;
		await fetch(url, {method : "POST"});
	}
	
	document.getElementById("amountEntry").value = 0;
	
	cleanTable();
	populateAccountTable();
}

