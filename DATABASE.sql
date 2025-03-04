CREATE TABLE tblResourceMap
(
	id varchar(30) PRIMARY KEY,
	systemPath varchar(50) NOT NULL
)

CREATE TABLE tblUser
(
	id int PRIMARY KEY,
	email nvarchar(50) NOT NULL,
	username varchar(30) NOT NULL UNIQUE,
	phoneNumber varchar(12) not null UNIQUE,
	password varchar(50) NOT NULL,
	persistentCookie varchar(255) UNIQUE,
	googleID varchar(255) unique,	
	facebookID varchar(255) unique,
	isAdmin bit DEFAULT 0,
	credit money,

	displayName nvarchar(50),
	profileStringResourceID varchar(30),
	bio nvarchar(255),

	CONSTRAINT fk_user_resourceID FOREIGN KEY (profileStringResourceID) REFERENCES tblResourceMap(id)
)

CREATE TABLE tblUserPreference
(
	userID int,
	preference nvarchar(30),

	PRIMARY KEY (userID, preference),
	CONSTRAINT fk_userpref_user FOREIGN KEY (userID) REFERENCES tblUser(id)
)

CREATE TABLE tblChatBox
(
	id int PRIMARY KEY,
	user1 int,
	user2 int,

	CONSTRAINT fk_chatbox_user1 FOREIGN KEY (user1) REFERENCES tblUser(id),
	CONSTRAINT fk_chatbox_user2 FOREIGN KEY (user2) REFERENCES tblUser(id)
)

CREATE TABLE tblChatBoxContent
(
	id int PRIMARY KEY,
	chatBoxID int,
	message nvarchar(100) NOT NULL,
	time DATETIME NOT NULL DEFAULT GETDATE(),
	senderID int,

	CONSTRAINT fk_cbcontent_sender FOREIGN KEY (senderID) REFERENCES tblUser(id),
	CONSTRAINT fk_cbcontent_chatbox FOREIGN KEY (chatBoxID) REFERENCES tblChatBox(id)
)

CREATE TABLE tblPromotion
(
	id int PRIMARY KEY,
	creatorID int,
	name nvarchar(30) NOT NULL,
	type bit DEFAULT 0, -- 0 is %, 1 is flat
	ofAdmin bit DEFAULT 0, 
	value int NOT NULL,
	creationDate DATE DEFAULT GETDATE(),
	expireDate DATE NOT NULL,

	CONSTRAINT fk_promo_creator FOREIGN KEY (creatorID) REFERENCES tblUser(id)
)

CREATE TABLE tblShop
(
	id int PRIMARY KEY,
	ownerID int,
	name nvarchar(30) NOT NULL,
	address nvarchar(100),
	profileStringResourceID varchar(30),
	visible bit DEFAULT 0,

	CONSTRAINT fk_shop_resourceID FOREIGN KEY (profileStringResourceID) REFERENCES tblResourceMap(id),
	CONSTRAINT fk_shop_owner FOREIGN KEY (ownerID) REFERENCES tblUser(id)
)

CREATE TABLE tblCategory
(
	id int PRIMARY KEY,
	name nvarchar(30),
	imageStringResourceID varchar(30),
	parent_id int

	CONSTRAINT fk_category_resourceID FOREIGN KEY (imageStringResourceID) REFERENCES tblResourceMap(id),
	CONSTRAINT fk_category_parentID FOREIGN KEY (parent_id) REFERENCES tblCategory(id)
)

CREATE TABLE tblVariation
(
	id int PRIMARY KEY,
	categoryID int,
	name nvarchar(30),
	datatype varchar(10),
	unit varchar(10),

	CONSTRAINT fk_variation_category FOREIGN KEY (categoryID) REFERENCES tblCategory(id)
)

CREATE TABLE tblVariationValue
(
	id int PRIMARY KEY,
	variationID int,
	value varchar(15),

	constraint fk_variationvalue_variation foreign key (variationID) references tblVariation(id)
)

CREATE TABLE tblProduct
(
	id int PRIMARY KEY,
	shopID int,
	categoryID int,
	name nvarchar(50) NOT NULL,
	description nvarchar(255),
	availablePromotionID int,
	imageStringResourceID varchar(30),
	
	CONSTRAINT fk_product_resourceID FOREIGN KEY (imageStringResourceID) REFERENCES tblResourceMap(id),
	CONSTRAINT fK_product_shop FOREIGN KEY (shopID) REFERENCES tblShop(id),
	CONSTRAINT fk_product_category FOREIGN KEY (categoryID) REFERENCES tblCategory(id),
	CONSTRAINT fk_product_promo FOREIGN KEY (availablePromotionID) REFERENCES tblPromotion(id)
)

CREATE TABLE tblProductImage 
(
	id int PRIMARY KEY,
	productID int,
	imageStringResourceID varchar(30),

	CONSTRAINT fk_productimage_resourceID FOREIGN KEY (imageStringResourceID) REFERENCES tblResourceMap(id),
	CONSTRAINT fk_productimage_product FOREIGN KEY (productID) REFERENCES tblProduct
)

create table tblProductItem
(
	id int primary key,
	productID int,
	stock int,
	price money,

	constraint fk_productitem_product foreign key (productID) references tblProduct(id)
)

create table tblProductCustomization
(
	id int primary key,
	productItemID int,
	variationValueID int,

	constraint fk_productcustomization_productitem foreign key (productItemID) references tblProductItem(id),
	constraint fk_productcustomization_variationvalue foreign key (variationValueID) references tblVariationValue(id)
)

create table tblReview
(
	id int PRIMARY KEY,
	userID int,
	productID int,
	rate int,
	comment nvarchar(255),

	constraint fk_review_user FOREIGN KEY (userID) REFERENCES tblUser(id),
	constraint fk_review_product FOREIGN KEY (productID) REFERENCES tblProduct(id)
)

CREATE TABLE tblCart
(
	id int PRIMARY KEY,
	userID int,

	CONSTRAINT fk_cart_user FOREIGN KEY (userID) REFERENCES tblUser(id)
)

CREATE TABLE tblCartItem
(
	id int PRIMARY KEY,
	cartID int,
	productItemID int,
	quantity int NOT NULL,

	-- CONSTRAINT pk_cartitem PRIMARY KEY (cartID, productCustomizationID),
	CONSTRAINT fk_cartitem_cart FOREIGN KEY (cartID) REFERENCES tblCart(id),
	CONSTRAINT fk_cartitem_productitem FOREIGN KEY (productItemID) REFERENCES tblProductItem(id)
)

CREATE TABLE tblPaymentMethod
(
	id int PRIMARY KEY,
	name varchar(30)
)

CREATE TABLE tblOrder
(
	id int PRIMARY KEY,
	userID int,
	paymentMethodID int,
	shippingAddress nvarchar(100),
	promotionID int,
	date datetime DEFAULT GETDATE(),
	finalPrice money,  --after promotion and iclude shipping cost


	CONSTRAINT fk_order_user FOREIGN KEY (userID) REFERENCES tblUser(id),
	CONSTRAINT fK_order_paymentMethod FOREIGN KEY (paymentMethodID) REFERENCES tblPaymentMethod(id),
	CONSTRAINT fk_order_promotion FOREIGN KEY (promotionID) REFERENCES tblPromotion(id)
)

CREATE TABLE tblOrderedItem
(
	id int PRIMARY KEY,
	orderID int,
	productItemID int,
	orderStatus varchar(30),
	quantity int NOT NULL,
	totalPrice money,
	shippingCost money, --per shop per order

	CONSTRAINT fk_ordereditem_order FOREIGN KEY (orderID) REFERENCES tblOrder(id),
	CONSTRAINT fk_ordereditem_productitem FOREIGN KEY (productItemID) REFERENCES tblProductItem(id)
)

CREATE TABLE tblOnholdCredit
(
	id int PRIMARY KEY,
	userID int,
	amount money NOT NULL,
	date date DEFAULT GETDATE(),
	claimDate date DEFAULT DATEADD(day, 7, GETDATE()),

	CONSTRAINT fk_onhold_user FOREIGN KEY (userID) REFERENCES tblUser(id)
)

CREATE TABLE tblNotification
(
	id int PRIMARY KEY,
	userID int, -- can be null if the notification is global
	title nvarchar(30) NOT NULL,
	body nvarchar(255),

	CONSTRAINT fk_notification_user FOREIGN KEY (userID) REFERENCES tblUser
)

CREATE TABLE tblServerStatistics
(
	id int PRIMARY KEY IDENTITY(1, 1), -- stats wont be deleted
	day date NOT NULL,
	totalMoneyEarned int,
	userNum int,
	productNum int,
	shopNum int,
	promotionNum int,
	purchaseNum int,
	visitNum int,
	peakSessionNum int,
	averageResponseTime int, -- in milisec
	maxResponseTime int
)

CREATE TABLE tblShopStatistics
(
	id int PRIMARY KEY,


)

-- TF-IDF RELATED STUFF
CREATE TABLE tblBaseVector 
(
    keyword nvarchar(400) PRIMARY KEY
);

CREATE TABLE tblVector (
    id int PRIMARY KEY,
    vector nvarchar(400)
);

GO
CREATE PROCEDURE ComputeTFIDF
AS
BEGIN
	DELETE FROM tblVector;
	DELETE FROM tblBaseVector;

	INSERT INTO tblBaseVector (keyword)
	(
		SELECT DISTINCT TRIM(value) AS keyword 
		FROM tblProduct
		CROSS APPLY STRING_SPLIT(LOWER(tblProduct.name), ' ')
		WHERE LEN(value) > 0

		UNION

		SELECT DISTINCT TRIM(value) AS keyword
		FROM tblProduct
		CROSS APPLY STRING_SPLIT(LOWER(tblProduct.description), ' ')
		WHERE LEN(value) > 0
	)

	WITH tfbefore AS (
		SELECT 
			p.id, 
			TRIM(s.value) AS keyword, 
			COUNT(*) * 0.7 / NULLIF((SELECT COUNT(*) FROM STRING_SPLIT(LOWER(p.name), ' ')), 0) AS tf
		FROM tblProduct p
		CROSS APPLY STRING_SPLIT(LOWER(p.name), ' ') s
		GROUP BY p.id, s.value, p.name

		UNION ALL

		SELECT 
			p.id, 
			TRIM(s.value) AS keyword, 
			COUNT(*) * 0.3 / NULLIF((SELECT COUNT(*) FROM STRING_SPLIT(LOWER(p.description), ' ')), 0) AS tf
		FROM tblProduct p
		CROSS APPLY STRING_SPLIT(LOWER(p.description), ' ') s
		GROUP BY p.id, s.value, p.description
	),
	tf_combined AS (
		SELECT 
			id, 
			keyword, 
			SUM(tf) AS tf 
		FROM tfbefore
		GROUP BY id, keyword
	),
	tf AS (
		SELECT b.id, a.keyword AS keyword, COALESCE(b2.tf, 0) tf
		FROM (SELECT DISTINCT keyword FROM tblBaseVector) a
		CROSS JOIN (SELECT DISTINCT id FROM tf_combined) b
		LEFT JOIN tf_combined b2 ON b.id = b2.id AND a.keyword = b2.keyword
	),
	df AS (
		SELECT keyword, COUNT(DISTINCT id) AS DF FROM tf_combined GROUP BY keyword
	),
	tfidf AS (
		SELECT 
			tf.id, 
			tf.keyword, 
			tf.TF * LOG((SELECT COUNT(*) FROM tblProduct) / NULLIF(df.DF, 0)) AS TFIDF
		FROM tf
		JOIN df ON tf.keyword = df.keyword
	),
	vectors AS (
		SELECT 
			id, 
			STRING_AGG(CAST(COALESCE(tfidf, 0) AS NVARCHAR), ',') WITHIN GROUP (ORDER BY tblBaseVector.keyword) AS Vector
		FROM tblBaseVector
		LEFT JOIN tfidf ON tblBaseVector.keyword = tfidf.keyword AND tfidf.id = id  -- Fixed join condition
		GROUP BY id
	)
	INSERT INTO tblVector
	SELECT * FROM vectors;
END;
GO

GO
CREATE PROCEDURE GetRecommendation (@query NVARCHAR(400))
AS
BEGIN
    CREATE TABLE #result (value varchar(400));
    
    WITH tf AS (
		SELECT b.keyword, COUNT(s.value) * 1.0 / NULLIF((SELECT COUNT(*) FROM STRING_SPLIT(@query, ' ')), 0) AS TF
        FROM STRING_SPLIT(@query, ' ') s
        RIGHT JOIN tblBaseVector b ON b.keyword = s.value
        GROUP BY b.keyword
	),
	df AS (
    --SELECT s.value AS keyword, COUNT(*) AS DF FROM STRING_SPLIT(@query, ' ') s GROUP BY s.value
	SELECT keyword, COUNT(DISTINCT id) AS DF FROM (SELECT 
													id,
													TRIM(s.value) AS keyword
													FROM tblProduct
													CROSS APPLY STRING_SPLIT(LOWER(tblProduct.description), ' ') s
													GROUP BY tblProduct.id, s.value, tblProduct.description) tbl
	GROUP BY keyword
	),
	tfidf AS (
		SELECT 
			tf.keyword, 
			tf.TF * LOG((SELECT COUNT(*) FROM tblProduct) / NULLIF(df.DF, 0)) AS TFIDF
		FROM tf
		JOIN df ON tf.keyword = df.keyword
	),
	vectors AS (
    SELECT
        STRING_AGG(CAST(COALESCE(tfidf, 0) AS NVARCHAR), ',') WITHIN GROUP (ORDER BY tblBaseVector.keyword) AS vector
    FROM tblBaseVector
    LEFT JOIN tfidf ON tblBaseVector.keyword = tfidf.keyword AND tblBaseVector.keyword = tfidf.keyword
	)
	INSERT INTO #result
    SELECT * FROM vectors;

    --SELECT * FROM STRING_SPLIT((SELECT value FROM #result), ',');

	WITH ItemVector AS (
		SELECT v.id, s.value AS tfidf_value, 
           ROW_NUMBER() OVER (PARTITION BY v.id ORDER BY (SELECT NULL)) AS pos
		FROM tblVector v
		CROSS APPLY STRING_SPLIT(v.vector, ',') s
	),
	QueryVector AS (
		SELECT s.value AS tfidf_value, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS pos
		FROM STRING_SPLIT((SELECT value FROM #result), ',') s
	),
	recommendation AS(
		SELECT TOP 10 iv.id,
		SUM(CAST(qv.tfidf_value AS FLOAT) * CAST(iv.tfidf_value AS FLOAT)) /
		(SQRT(SUM(POWER(CAST(iv.tfidf_value AS FLOAT), 2))) * SQRT(SUM(POWER(CAST(qv.tfidf_value AS FLOAT), 2)))) AS similarity
		FROM ItemVector iv
		JOIN QueryVector qv ON iv.pos = qv.pos
		GROUP BY iv.id
	)
	SELECT tblProduct.*
	FROM tblProduct
	JOIN recommendation ON tblProduct.id = recommendation.id
	ORDER BY recommendation.similarity DESC

    DROP TABLE #result;
END;
GO
