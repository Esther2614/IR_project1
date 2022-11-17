<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="./css/main.css">
		<link rel="stylesheet" href="./css/bootstrap.min.css?<?php echo time(); ?">
		<title>Search</title>
	</head>

	<body>
		<div class="main">
			<div class="logo">
				<img alt="logo" src="logo.png">
			</div>
			<div class="searchbox">
				<div class="searchform">
					<form action="search?p=1" method="get">
						<input type="text" name="query" id="query" value=" ">
						<input type="submit" value="搜索一下">
					</form>
				</div>
			</div>
			<form action="search?p=1" method="get">
				<input type="text" name="query" id="query" value=" ">
				<input type="image" src="search_line_icon.png" name="submit" class="mg">
			</form>

			<div class="footer">
				<ul>
					<li>信息检索</li>
					<li>搜索引擎</li>
				</ul>
			</div>
		</div>
	</body>

	</html>