<html>
<head>
  <title>Flight log system notification failure</title>
</head>
<body>

  <#if (applicationException.messageList?size gt 0)>
     <span style="font-style: underline;">Processing failed with the following exceptions:</span>
	  <#list applicationException.messageList as message>
		  <ul style="font-style: italic;">
		    <li>${message}</li>
		  </ul>
	  </#list>
  </#if>

</body>
</html>