 //----- month id generation function---///

function monthID_generation(label){
	
	if (label== "JAN")
		return 1;
	else if (label== "FEB")
		return 2;
	else if (label== "MAR")
		return 3;
	else if (label== "APR")
		return 4;
	else if (label== "MAY")
		return 5;
	else if (label== "JUN")
		return 6;
	else if (label== "JUL")
		return 7;
	else if (label== "AUG")
		return 8;
	else if (label== "SEP")
		return 9;
	else if (label== "OCT")
		return 10;
	else if (label== "NOV")
		return 11;
	else  (label== "DEC")
		return 12;
	
}

//-------------------------------- status generation----------------------------//

function generateStatus(stat)
{
	if(stat == "init_Cnt")
		return "INITIATED";
	else if(stat == "appr_Cnt")
		return "APPROVED";
	else if(stat == "board_Cnt")
		return "BOARDED";
	else (stat == "reject_Cnt")
		return "REJECTED";
	}
//-------------------------------month name generation---------------------------------//

function month_generation(label){
	
	if (label== "JAN")
		return "JANUARY";
	else if (label== "FEB")
		return "FEBRUARY";
	else if (label== "MAR")
		return "MARCH";
	else if (label== "APR")
		return "APRIL";
	else if (label== "MAY")
		return "MAY";
	else if (label== "JUN")
		return "JUNE";
	else if (label== "JUL")
		return "JULY";
	else if (label== "AUG")
		return "AUGUST";
	else if (label== "SEP")
		return "SEPTEMBER";
	else if (label== "OCT")
		return "OCTOBER";
	else if (label== "NOV")
		return "NOVEMBER";
	else  (label== "DEC")
		return "DECEMBER";
	
}


function month_generation_grid(label){
	
	if (label== 1)
		return "JANUARY";
	else if (label== 2)
		return "FEBRUARY";
	else if (label== 3)
		return "MARCH";
	else if (label== 4)
		return "APRIL";
	else if (label== 5)
		return "MAY";
	else if (label== 6)
		return "JUNE";
	else if (label== 7)
		return "JULY";
	else if (label== 8)
		return "AUGUST";
	else if (label== 9)
		return "SEPTEMBER";
	else if (label== 10)
		return "OCTOBER";
	else if (label== 11)
		return "NOVEMBER";
	else  (label== 12)
		return "DECEMBER";
	
} 