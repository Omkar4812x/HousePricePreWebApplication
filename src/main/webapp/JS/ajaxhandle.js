let ajaxTest=()=>{
	let nm = document.getElementById("name").value;
	let xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function()
	{
		if(this.readyState==4 && this.status==200)
			{
				document.getElementById("h").innerHTML=this.responseText;
			}
	}
	
	xhttp.open("GET","/demo?name="+nm,true);
	xhttp.send();
}