<html>
<head>
  <title>Upcoming due components notification</title>
</head>
<body>

  <h3>The following
	  <#if (upcominDueComponents?size == 1)>
	  component
	  </#if>
	  <#if (upcominDueComponents?size > 1)>
	  ${upcominDueComponents?size} components
	  </#if>
  will be due by ${dateDue?date} or ${hoursDue} hrs:</h3>


<#--

  <#if (priceHoldingsExceptions.messageList?size gt 0)>
     <span style="text-decoration: underline;">The balance below is not accurate as there are exceptions during pricing. See below:</span>
      <ul style="font-style: italic;">
	  <#list priceHoldingsExceptions.messageList as message>
		    <li>${message}</li>
	  </#list>
      </ul>
  </#if>


  <h3>Market value as of ${todaysSnapshot?datetime} is
	  <#if (priceHoldingsExceptions.messageList?size == 0)>
	    ${todaysMarketValue?string.currency}
	  <#else>
	    <span style="opacity: 0.7;">${todaysMarketValue?string.currency}</span> 
	  </#if>
  </h3>   
  
  Change of <b>
  <#if percentChange lt 0>
  <span style="color: red;"><img src="${downArrowUrl}" style="vertical-align: middle;" alt="Down arrow">${percentChange}%</span>
  <#else>
  <span style="color: green;"><img src="${upArrowUrl}" style="vertical-align: middle;" alt="Up arrow">${percentChange}%</span>
  </#if>
  </b>
  
-->

  
  <table style="margin-top: 1rem;" cellspacing="10">
  <tr>
    <th>Component name</th>
    <th>Component desc</th>
    <th>Date performed</th>
    <th>Date due</th>
    <th>Hours performed</th>
    <th>Hours due</th>
  </tr>
  <#list upcominDueComponents as upcominDueComponent>
	  <tr>
	    <td>${upcominDueComponent.name}</td>
	    <td>${upcominDueComponent.description!}</td>
	    <td>${(upcominDueComponent.datePerformed?date)!}</td>
	    <td>${(upcominDueComponent.dateDue?date)!}</td>
	    <td>${upcominDueComponent.hoursDue!}</td>
	    <td>${upcominDueComponent.hoursPerformed!}</td>
	  </tr>
  </#list>
  </table>


</body>
</html>
