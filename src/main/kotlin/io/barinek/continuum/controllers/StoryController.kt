package io.barinek.continuum.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import io.barinek.continuum.dataaccess.StoryDataGateway
import io.barinek.continuum.models.StoryInfo
import io.barinek.continuum.util.BasicHandler
import org.eclipse.jetty.server.Request
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class StoryController(val mapper: ObjectMapper, val gateway: StoryDataGateway) : BasicHandler() {

    override fun handle(s: String, request: Request, httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        post("/stories", request, httpServletResponse) {
            val story = mapper.readValue(request.reader, StoryInfo::class.java)
            val record = gateway.create(story.projectId, story.name)
            mapper.writeValue(httpServletResponse.outputStream, StoryInfo(record.id, record.projectId, record.name, "story info"))
        }
        get("/stories", request, httpServletResponse) {
            val projectId = request.getParameter("projectId")
            val list = gateway.findBy(projectId.toLong()).map { record ->
                StoryInfo(record.id, record.projectId, record.name, "story info")
            }
            mapper.writeValue(httpServletResponse.outputStream, list)
        }
    }

}