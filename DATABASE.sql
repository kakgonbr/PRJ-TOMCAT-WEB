CREATE TABLE tblResourceMap
(
	id varchar(30) PRIMARY KEY,
	systemPath varchar(50) NOT NULL
)

CREATE TABLE tblUser
(
	id int PRIMARY KEY,
	email nvarchar(50) NOT NULL UNIQUE,
	username varchar(30) NOT NULL UNIQUE,
	phoneNumber varchar(12) UNIQUE,
	password varchar(50) NOT NULL,
	persistentCookie varchar(255),
	googleID varchar(255) UNIQUE,
	facebookID varchar(255) UNIQUE,
	isAdmin bit DEFAULT 0,
	credit money,

	displayName nvarchar(50),
	profileStringResourceID varchar(30),
	bio nvarchar(255),

	CONSTRAINT fk_user_resourceID FOREIGN KEY (profileStringResourceID) REFERENCES tblResourceMap
)

CREATE TABLE tblUserPreference
(
	userID int,
	preference nvarchar(30),

	PRIMARY KEY (userID, preference),
	CONSTRAINT fk_userpref_user FOREIGN KEY (userID) REFERENCES tblUser
)

CREATE TABLE tblChatBox
(
	id int PRIMARY KEY,
	user1 int,
	user2 int,

	CONSTRAINT fk_chatbox_user1 FOREIGN KEY (user1) REFERENCES tblUser,
	CONSTRAINT fk_chatbox_user2 FOREIGN KEY (user2) REFERENCES tblUser
)

CREATE TABLE tblChatBoxContent
(
	id int PRIMARY KEY,
	chatBoxID int,
	message nvarchar(100) NOT NULL,
	time DATETIME NOT NULL DEFAULT GETDATE(),
	senderID int,

	CONSTRAINT fk_cbcontent_sender FOREIGN KEY (senderID) REFERENCES tblUser,
	CONSTRAINT fk_cbcontent_chatbox FOREIGN KEY (chatBoxID) REFERENCES tblChatBox
)

CREATE TABLE tblPromotion
(
	id int PRIMARY KEY,
	creatorID int,
	name nvarchar(30) NOT NULL,
	type bit DEFAULT 0, -- 0 is %, 1 is flat
	isForCart bit DEFAULT 0, 
	value int NOT NULL,
	creationDate DATE DEFAULT GETDATE(),
	expireDate DATE NOT NULL,

	CONSTRAINT fk_promo_creator FOREIGN KEY (creatorID) REFERENCES tblUser
)

CREATE TABLE tblShop
(
	id int PRIMARY KEY,
	ownerID int,
	name nvarchar(30) NOT NULL,
	address nvarchar(100),
	profileStringResourceID varchar(30),
	visible bit DEFAULT 0,

	CONSTRAINT fk_shop_resourceID FOREIGN KEY (profileStringResourceID) REFERENCES tblResourceMap,
	CONSTRAINT fk_shop_owner FOREIGN KEY (ownerID) REFERENCES tblUser
)

CREATE TABLE tblCategory
(
	id int PRIMARY KEY,
	name nvarchar(30),
	imageStringResourceID varchar(30),

	CONSTRAINT fk_category_resourceID FOREIGN KEY (imageStringResourceID) REFERENCES tblResourceMap
)

CREATE TABLE tblVariation
(
	id int PRIMARY KEY,
	categoryID int,
	name nvarchar(30),
	datatype varchar(10), -- TREAT AS ENUM

	CONSTRAINT fk_variation_category FOREIGN KEY (categoryID) REFERENCES tblCategory
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

	CONSTRAINT fk_product_resourceID FOREIGN KEY (imageStringResourceID) REFERENCES tblResourceMap,
	CONSTRAINT fK_product_shop FOREIGN KEY (shopID) REFERENCES tblShop,
	CONSTRAINT fk_product_category FOREIGN KEY (categoryID) REFERENCES tblCategory,
	CONSTRAINT fk_product_promo FOREIGN KEY (availablePromotionID) REFERENCES tblPromotion
)

CREATE TABLE tblProductCustomization
(
	id int PRIMARY KEY,
	productID int,
	variationID int,
	data varchar(20) NOT NULL

	-- CONSTRAINT pk_productcustomization PRIMARY KEY (productID, variationID),
	CONSTRAINT fk_productcustomization_product FOREIGN KEY (productID) REFERENCES tblProduct,
	CONSTRAINT fk_productcustomization_variation FOREIGN KEY (variationID) REFERENCES tblVariation
)

CREATE TABLE tblCart
(
	id int PRIMARY KEY,
	userID int,

	CONSTRAINT fk_cart_user FOREIGN KEY (userID) REFERENCES tblUser
)

CREATE TABLE tblCartItem
(
	id int PRIMARY KEY,
	cartID int,
	productCustomizationID int,
	quantity int NOT NULL,

	-- CONSTRAINT pk_cartitem PRIMARY KEY (cartID, productCustomizationID),
	CONSTRAINT fk_cartitem_cart FOREIGN KEY (cartID) REFERENCES tblCart,
	CONSTRAINT fk_cartitem_productcustomization FOREIGN KEY (productCustomizationID) REFERENCES tblProductCustomization
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

	CONSTRAINT fk_order_user FOREIGN KEY (userID) REFERENCES tblUser,
	CONSTRAINT fK_order_paymentMethod FOREIGN KEY (paymentMethodID) REFERENCES tblPaymentMethod,
	CONSTRAINT fk_order_promotion FOREIGN KEY (promotionID) REFERENCES tblPromotion
)

CREATE TABLE tblOrderedItem
(
	productCustomizationID int,
	orderID int,
	orderStatus varchar(30),
	quantity int NOT NULL,

	CONSTRAINT fk_ordereditem_order FOREIGN KEY (orderID) REFERENCES tblOrder,
	CONSTRAINT fk_ordereditem_productcustomization FOREIGN KEY (productCustomizationID) REFERENCES tblProductCustomization
)

CREATE TABLE tblOnholdCredit
(
	userID int,
	amount money NOT NULL,
	date date DEFAULT GETDATE(),
	claimDate date DEFAULT DATEADD(day, 7, GETDATE()),

	CONSTRAINT fk_onhold_user FOREIGN KEY (userID) REFERENCES tblUser
)

--ALTER TABLE tblUserPreference
--ADD CONSTRAINT fk_userpref_user FOREIGN KEY (userID) REFERENCES tblUser
