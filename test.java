private DefaultMutableTreeNode buildTreeNode(Node rootNode) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(
            rootNode.getNodeName());

    if (rootNode.hasAttributes()) {
        NamedNodeMap attributes = rootNode.getAttributes();

        for (int j = 0; j < attributes.getLength(); j++) {
            String attr = attributes.item(j).toString();
            treeNode.add(new DefaultMutableTreeNode("@" + attr));
        }
    }

    if (rootNode.hasChildNodes()) {
        NodeList children = rootNode.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            short nodeType = node.getNodeType();

            if (nodeType == Node.ELEMENT_NODE)
                treeNode.add(buildTreeNode(node));

            else if (nodeType == Node.TEXT_NODE) {
                String text = node.getTextContent().trim();
                if (!text.equals(""))
                    treeNode.add(new DefaultMutableTreeNode(text));

            } else if (nodeType == Node.COMMENT_NODE) {
                String comment = node.getNodeValue().trim();
                treeNode.add(new DefaultMutableTreeNode("#" + comment));
            }
        }
    }
    return treeNode;