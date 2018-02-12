-- Love2D based graph visualization tool
function love.load()

	love.window.setMode(400, 400)

	graphFile = 'graph.g'
	colorFile = 'colors.c'

	graph = {}
	colorList = {}

	lineNum = 0

	for i=0,49 do
		table.insert(graph, newNode(i))
	end

	--load graph file
	for line in love.filesystem.lines(graphFile) do
		if(lineNum > 0) then
			ii = 0
			for i in string.gmatch(line, "%S+") do
				if(ii == 0) then
					s = i
					ii = 1
				else
					d = i
					ii = 0
				end
			end
			source = tonumber(s)
			dest = tonumber(d)
			print("source: " .. source .. ", dest: " .. dest)
			if(not contains(graph, source)) then
				table.insert(graph, newNode(source))
			end
			if(not contains(graph, dest)) then
				table.insert(graph, newNode(dest))
			end

			sNode = getNode(graph, source)
			dNode = getNode(graph, dest)

			if(not contains(sNode.neighbors, dest)) then
				table.insert(sNode.neighbors, dNode)
			end
			if(not contains(dNode.neighbors, source)) then
				table.insert(dNode.neighbors, sNode)
			end
		else
			numColors = tonumber(line)
			print(numColors)
			for i=1,numColors do
				table.insert(colorList, {r = 100 + love.math.random(155), g = 50 + love.math.random(205), b = 50 + love.math.random(205)})
			end
		end
		lineNum = lineNum + 1
	end

	colorList[1].r = 255
	colorList[1].g = 0
	colorList[1].b = 0

	colorList[2].r = 0
	colorList[2].g = 255
	colorList[2].b = 0

	colorList[3].r = 0
	colorList[3].g = 0
	colorList[3].b = 255

	--load color files
	id = 1
	for line in love.filesystem.lines(colorFile) do
		c = tonumber(line) + 1
		node = getNode(graph, id)
		print(c)
		node.color = c
		id = id + 1
	end

end

function love.draw()

	wWidth = love.window.getWidth()
	wHeight = love.window.getHeight()
	love.graphics.rectangle('fill',0,0,wWidth,wHeight)

	for i,node in ipairs(graph) do
		node:drawEdges(colorList)
	end

	for i, node in ipairs(graph) do
		node:drawNode(colorList)
	end

end

function love.update()

end







-- helper functions
-- Node class description
function newNode(id)
	node = {}
	node.neighbors = {}
	node.color = 1
	node.id = id
	wWidth = love.window.getWidth()
	wHeight = love.window.getHeight()
	node.x = love.math.random(wWidth - 20) + 10
	node.y = love.math.random(wHeight - 20) + 10
	node.r = 14

	node.drawNode = function(self, colorList)

		love.graphics.setColor(colorList[self.color].r, colorList[self.color].g, colorList[self.color].b)
		love.graphics.circle('fill', self.x, self.y, self.r)
		love.graphics.setColor(255, 255, 255, 100)
		love.graphics.setColor(255, 255, 255)

	end

	node.drawEdges = function(self, colorList)
		love.graphics.setColor(0, 0, 0, 100)
		for i,neighbor in ipairs(self.neighbors) do
			love.graphics.line(self.x, self.y, neighbor.x, neighbor.y)
		end
	end

	return node
end

function contains(graph, nodeID)
	for i,n in ipairs(graph) do
		if(graph[i].id == nodeID) then
			return true
		end
	end
	return false
end


function getNode(graph, nodeID)
	for i,n in ipairs(graph) do
		if(graph[i].id == nodeID) then
			return graph[i]
		end
	end
	return {}
end

function love.keypressed()
	print(love.filesystem.getSaveDirectory())
    local name = string.format("screenshot-%s.png", os.date("%Y%m%d-%H%M%S"))
  	local shot = love.graphics.newScreenshot()
  	shot:encode(name, "png")
end