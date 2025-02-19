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
	password varchar(50) NOT NULL UNIQUE,
	persistentCookie varchar(255),
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
	id int primary key,
	userID int,
	productID int,
	rate int,
	comment nvarchar(255),

	constraint fk_review_user foreign key (userID) references tblUser(id),
	constraint fk_review_product foreign key (productID) references tblProduct(id)
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
	id int primary key,
	orderID int,
	productItemID int,
	orderStatus varchar(30),
	quantity int NOT NULL,
	totalPrice money,

	CONSTRAINT fk_ordereditem_order FOREIGN KEY (orderID) REFERENCES tblOrder(id),
	CONSTRAINT fk_ordereditem_productitem FOREIGN KEY (productItemID) REFERENCES tblProductItem(id)
)

CREATE TABLE tblOnholdCredit
(
	id int primary key,
	userID int,
	amount money NOT NULL,
	date date DEFAULT GETDATE(),
	claimDate date DEFAULT DATEADD(day, 7, GETDATE()),

	CONSTRAINT fk_onhold_user FOREIGN KEY (userID) REFERENCES tblUser(id)
)
